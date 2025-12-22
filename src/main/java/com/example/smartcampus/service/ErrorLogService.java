package com.example.smartcampus.service;

import com.example.smartcampus.dto.ErrorLogDTO;
import com.example.smartcampus.dto.ErrorLogQueryDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ErrorLogService {

    // 记录错误日志
    void logError(String module, String className, String methodName,
                  Throwable throwable, String level, Object... context);

    // 记录错误日志（带请求信息）
    void logErrorWithRequest(String module, String className, String methodName,
                             Throwable throwable, String level,
                             String requestUrl, String requestMethod,
                             String clientIp, String requestParams,
                             Long userId, String username);

    // 根据ID查询错误日志
    ErrorLogDTO getErrorLogById(Long id);

    // 多条件分页查询错误日志
    Page<ErrorLogDTO> getErrorLogs(ErrorLogQueryDTO queryDTO);

    // 删除错误日志
    void deleteErrorLog(Long id);

    // 批量删除过期错误日志
    void deleteExpiredErrorLogs(LocalDateTime beforeTime);

    // 每月清理任务
    void cleanupMonthly();

    // 按天数清理错误日志
    void cleanupByDays(int days);

    // 获取错误统计信息
    Map<String, Object> getErrorStatistics();

    // 获取模块错误统计
    Map<String, Long> getModuleErrorStats();

    // 获取错误级别统计
    Map<String, Long> getLevelErrorStats();

    // 获取错误趋势
    Map<String, Long> getErrorTrend(int days);
}