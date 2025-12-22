package com.example.smartcampus.service.impl;

import com.example.smartcampus.dto.ErrorLogDTO;
import com.example.smartcampus.dto.ErrorLogQueryDTO;
import com.example.smartcampus.entity.ErrorLog;
import com.example.smartcampus.repository.ErrorLogRepository;
import com.example.smartcampus.service.ErrorLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

    private final ErrorLogRepository errorLogRepository;
    private final ObjectMapper objectMapper;

    public ErrorLogServiceImpl(ErrorLogRepository errorLogRepository, ObjectMapper objectMapper) {
        this.errorLogRepository = errorLogRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void logError(String module, String className, String methodName,
                         Throwable throwable, String level, Object... context) {

        // 获取当前用户信息
        String username = "SYSTEM";
        Long userId = null;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
                // 这里可以根据实际情况获取用户ID
            }
        } catch (Exception e) {
            // 忽略获取用户信息的异常
        }

        // 获取请求信息
        String requestUrl = null;
        String requestMethod = null;
        String clientIp = null;
        String requestParams = null;

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                requestUrl = request.getRequestURL().toString();
                requestMethod = request.getMethod();
                clientIp = getClientIpAddress(request);

                // 序列化请求参数（简化处理）
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (!parameterMap.isEmpty()) {
                    requestParams = objectMapper.writeValueAsString(parameterMap);
                }
            }
        } catch (Exception e) {
            // 忽略获取请求信息的异常
        }

        logErrorWithRequest(module, className, methodName, throwable, level,
                requestUrl, requestMethod, clientIp, requestParams, userId, username);
    }

    @Override
    public void logErrorWithRequest(String module, String className, String methodName,
                                    Throwable throwable, String level,
                                    String requestUrl, String requestMethod,
                                    String clientIp, String requestParams,
                                    Long userId, String username) {

        try {
            ErrorLog errorLog = new ErrorLog();
            errorLog.setModule(module);
            errorLog.setClassName(className);
            errorLog.setMethodName(methodName);
            errorLog.setErrorMessage(throwable.getMessage());
            errorLog.setStackTrace(getStackTrace(throwable));
            errorLog.setErrorType(throwable.getClass().getSimpleName());
            errorLog.setLevel(level);
            errorLog.setRequestUrl(requestUrl);
            errorLog.setRequestMethod(requestMethod);
            errorLog.setClientIp(clientIp);
            errorLog.setRequestParams(requestParams);
            errorLog.setUserId(userId);
            errorLog.setUsername(username != null ? username : "UNKNOWN");
            errorLog.setErrorTime(LocalDateTime.now());

            errorLogRepository.save(errorLog);

            // 同时输出到控制台（便于开发调试）
            System.err.println("[" + level + "] " + module + " - " + throwable.getMessage());

        } catch (Exception e) {
            // 记录错误日志本身的异常，避免循环错误
            System.err.println("记录错误日志失败: " + e.getMessage());
        }
    }

    @Override
    public ErrorLogDTO getErrorLogById(Long id) {
        ErrorLog errorLog = errorLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("错误日志不存在"));
        return convertToDTO(errorLog);
    }

    @Override
    public Page<ErrorLogDTO> getErrorLogs(ErrorLogQueryDTO queryDTO) {
        Pageable pageable = PageRequest.of(
                queryDTO.getPage(),
                queryDTO.getSize(),
                Sort.by(Sort.Direction.DESC, "errorTime")
        );

        Page<ErrorLog> errorLogs = errorLogRepository.findByConditions(
                queryDTO.getModule(),
                queryDTO.getClassName(),
                queryDTO.getErrorType(),
                queryDTO.getLevel(),
                queryDTO.getStartTime(),
                queryDTO.getEndTime(),
                queryDTO.getUsername(),
                pageable
        );

        return errorLogs.map(this::convertToDTO);
    }

    @Override
    public void deleteErrorLog(Long id) {
        if (!errorLogRepository.existsById(id)) {
            throw new RuntimeException("错误日志不存在");
        }
        errorLogRepository.deleteById(id);
    }

    @Override
    public void deleteExpiredErrorLogs(LocalDateTime beforeTime) {
        try {
            // 简单的删除逻辑，实际项目中可能需要更复杂的处理
            List<ErrorLog> expiredLogs = errorLogRepository.findByErrorTimeBefore(beforeTime);
            int deletedCount = expiredLogs.size();
            errorLogRepository.deleteAll(expiredLogs);
            System.out.println("已清理过期错误日志 " + deletedCount + " 条，时间早于: " + beforeTime);
        } catch (Exception e) {
            System.err.println("清理过期错误日志失败: " + e.getMessage());
        }
    }

    @Override
    public void cleanupMonthly() {
        try {
            // 计算30天前的时间
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

            List<ErrorLog> expiredLogs = errorLogRepository.findByErrorTimeBefore(thirtyDaysAgo);
            int deletedCount = expiredLogs.size();

            if (deletedCount > 0) {
                errorLogRepository.deleteAll(expiredLogs);
                System.out.println("[" + LocalDateTime.now() + "] 每月清理任务完成，已删除 " + deletedCount +
                        " 条30天前的错误日志");

                // 记录清理操作到日志（可选）
                logInfo("系统维护", "ErrorLogServiceImpl", "cleanupMonthly",
                        "已清理 " + deletedCount + " 条30天前的错误日志");
            } else {
                System.out.println("[" + LocalDateTime.now() + "] 每月清理任务完成，无需清理");
            }

        } catch (Exception e) {
            System.err.println("每月清理错误日志失败: " + e.getMessage());
            // 记录清理失败的错误
            logError("系统维护", "ErrorLogServiceImpl", "cleanupMonthly", e, "ERROR");
        }
    }

    @Override
    public void cleanupByDays(int days) {
        try {
            LocalDateTime targetTime = LocalDateTime.now().minusDays(days);
            List<ErrorLog> expiredLogs = errorLogRepository.findByErrorTimeBefore(targetTime);
            int deletedCount = expiredLogs.size();

            if (deletedCount > 0) {
                errorLogRepository.deleteAll(expiredLogs);
                System.out.println("已清理 " + deletedCount + " 条" + days + "天前的错误日志");
            }

        } catch (Exception e) {
            System.err.println("按天数清理错误日志失败: " + e.getMessage());
            logError("系统维护", "ErrorLogServiceImpl", "cleanupByDays", e, "ERROR");
        }
    }

    @Override
    public Map<String, Object> getErrorStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总错误数
        statistics.put("totalErrors", errorLogRepository.count());

        // 今日错误数
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        statistics.put("todayErrors", errorLogRepository.findByErrorTimeBetween(
                todayStart, LocalDateTime.now(), PageRequest.of(0, 1)).getTotalElements());

        // 模块错误统计
        statistics.put("moduleStats", getModuleErrorStats());

        // 级别错误统计
        statistics.put("levelStats", getLevelErrorStats());

        // 最近7天错误趋势
        statistics.put("recentTrend", getErrorTrend(7));

        return statistics;
    }

    @Override
    public Map<String, Long> getModuleErrorStats() {
        List<Object[]> moduleStats = errorLogRepository.countByModule();
        return moduleStats.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));
    }

    @Override
    public Map<String, Long> getLevelErrorStats() {
        List<Object[]> levelStats = errorLogRepository.countByLevel();
        return levelStats.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));
    }

    @Override
    public Map<String, Long> getErrorTrend(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<Object[]> trendData = errorLogRepository.countByDate(startDate);

        return trendData.stream()
                .collect(Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> (Long) arr[1]
                ));
    }

    // 工具方法：获取堆栈跟踪信息
    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    // 工具方法：获取客户端IP地址
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 对于多个IP的情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    // 添加一个记录信息日志的辅助方法
    private void logInfo(String module, String className, String methodName, String message) {
        try {
            Exception infoException = new Exception(message);
            logError(module, className, methodName, infoException, "INFO");
        } catch (Exception e) {
            // 忽略记录信息日志时的异常
        }
    }

    // 实体转DTO
    private ErrorLogDTO convertToDTO(ErrorLog errorLog) {
        if (errorLog == null) {
            return null;
        }

        ErrorLogDTO dto = new ErrorLogDTO();
        dto.setId(errorLog.getId());
        dto.setModule(errorLog.getModule());
        dto.setClassName(errorLog.getClassName());
        dto.setMethodName(errorLog.getMethodName());
        dto.setErrorMessage(errorLog.getErrorMessage());
        dto.setStackTrace(errorLog.getStackTrace());
        dto.setErrorType(errorLog.getErrorType());
        dto.setLevel(errorLog.getLevel());
        dto.setRequestUrl(errorLog.getRequestUrl());
        dto.setRequestMethod(errorLog.getRequestMethod());
        dto.setClientIp(errorLog.getClientIp());
        dto.setRequestParams(errorLog.getRequestParams());
        dto.setErrorTime(errorLog.getErrorTime());
        dto.setUserId(errorLog.getUserId());
        dto.setUsername(errorLog.getUsername());
        dto.setCreateTime(errorLog.getCreateTime());
        return dto;
    }
}