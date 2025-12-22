package com.example.smartcampus.controller;

import com.example.smartcampus.dto.ErrorLogDTO;
import com.example.smartcampus.dto.ErrorLogQueryDTO;
import com.example.smartcampus.service.ErrorLogService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/error-logs")
@CrossOrigin(origins = "*")
public class ErrorLogController {

    private final ErrorLogService errorLogService;

    public ErrorLogController(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    /**
     * 查询错误日志（分页）- 仅管理员可访问
     */
    @PostMapping("/query")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ErrorLogDTO>> getErrorLogs(@RequestBody ErrorLogQueryDTO queryDTO) {
        try {
            Page<ErrorLogDTO> errorLogs = errorLogService.getErrorLogs(queryDTO);
            return ResponseEntity.ok(errorLogs);
        } catch (Exception e) {
            throw new RuntimeException("查询错误日志失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取错误日志详情 - 仅管理员可访问
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ErrorLogDTO> getErrorLogById(@PathVariable Long id) {
        try {
            ErrorLogDTO errorLog = errorLogService.getErrorLogById(id);
            return ResponseEntity.ok(errorLog);
        } catch (Exception e) {
            throw new RuntimeException("获取错误日志详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取错误统计信息 - 仅管理员可访问
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getErrorStatistics() {
        try {
            Map<String, Object> statistics = errorLogService.getErrorStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            throw new RuntimeException("获取错误统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取模块错误统计 - 仅管理员可访问
     */
    @GetMapping("/statistics/module")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getModuleErrorStats() {
        try {
            Map<String, Long> stats = errorLogService.getModuleErrorStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            throw new RuntimeException("获取模块错误统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取错误级别统计 - 仅管理员可访问
     */
    @GetMapping("/statistics/level")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getLevelErrorStats() {
        try {
            Map<String, Long> stats = errorLogService.getLevelErrorStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            throw new RuntimeException("获取错误级别统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取错误趋势 - 仅管理员可访问
     */
    @GetMapping("/statistics/trend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getErrorTrend(
            @RequestParam(defaultValue = "7") int days) {
        try {
            Map<String, Long> trend = errorLogService.getErrorTrend(days);
            return ResponseEntity.ok(trend);
        } catch (Exception e) {
            throw new RuntimeException("获取错误趋势失败: " + e.getMessage());
        }
    }

    /**
     * 删除错误日志 - 仅管理员可访问
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteErrorLog(@PathVariable Long id) {
        try {
            errorLogService.deleteErrorLog(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("删除错误日志失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除错误日志 - 仅管理员可访问
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> batchDeleteErrorLogs(@RequestBody List<Long> ids) {
        try {
            for (Long id : ids) {
                errorLogService.deleteErrorLog(id);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("批量删除错误日志失败: " + e.getMessage());
        }
    }

    /**
     * 清理过期错误日志 - 仅管理员可访问
     */
    @DeleteMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cleanupExpiredErrorLogs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeTime) {
        try {
            errorLogService.deleteExpiredErrorLogs(beforeTime);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("清理过期错误日志失败: " + e.getMessage());
        }
    }

    /**
     * 手动触发每月清理任务 - 仅管理员可访问
     */
    @PostMapping("/cleanup/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> triggerMonthlyCleanup() {
        try {
            errorLogService.cleanupMonthly();
            return ResponseEntity.ok("每月清理任务已触发");
        } catch (Exception e) {
            throw new RuntimeException("触发每月清理任务失败: " + e.getMessage());
        }
    }

    /**
     * 按天数清理错误日志 - 仅管理员可访问
     */
    @PostMapping("/cleanup/days/{days}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cleanupByDays(@PathVariable int days) {
        try {
            errorLogService.cleanupByDays(days);
            return ResponseEntity.ok("已清理" + days + "天前的错误日志");
        } catch (Exception e) {
            throw new RuntimeException("清理错误日志失败: " + e.getMessage());
        }
    }

    /**
     * 获取简单的错误日志列表 - 仅管理员可访问
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ErrorLogDTO>> getErrorLogList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            ErrorLogQueryDTO queryDTO = new ErrorLogQueryDTO();
            queryDTO.setPage(page);
            queryDTO.setSize(size);
            Page<ErrorLogDTO> errorLogs = errorLogService.getErrorLogs(queryDTO);
            return ResponseEntity.ok(errorLogs);
        } catch (Exception e) {
            throw new RuntimeException("获取错误日志列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据模块查询错误日志 - 仅管理员可访问
     */
    @GetMapping("/module/{module}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ErrorLogDTO>> getErrorLogsByModule(
            @PathVariable String module,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            ErrorLogQueryDTO queryDTO = new ErrorLogQueryDTO();
            queryDTO.setModule(module);
            queryDTO.setPage(page);
            queryDTO.setSize(size);
            Page<ErrorLogDTO> errorLogs = errorLogService.getErrorLogs(queryDTO);
            return ResponseEntity.ok(errorLogs);
        } catch (Exception e) {
            throw new RuntimeException("根据模块查询错误日志失败: " + e.getMessage());
        }
    }

    /**
     * 根据错误级别查询错误日志 - 仅管理员可访问
     */
    @GetMapping("/level/{level}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ErrorLogDTO>> getErrorLogsByLevel(
            @PathVariable String level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            ErrorLogQueryDTO queryDTO = new ErrorLogQueryDTO();
            queryDTO.setLevel(level);
            queryDTO.setPage(page);
            queryDTO.setSize(size);
            Page<ErrorLogDTO> errorLogs = errorLogService.getErrorLogs(queryDTO);
            return ResponseEntity.ok(errorLogs);
        } catch (Exception e) {
            throw new RuntimeException("根据错误级别查询错误日志失败: " + e.getMessage());
        }
    }

    /**
     * 根据时间范围查询错误日志 - 仅管理员可访问
     */
    @GetMapping("/time-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ErrorLogDTO>> getErrorLogsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            ErrorLogQueryDTO queryDTO = new ErrorLogQueryDTO();
            queryDTO.setStartTime(startTime);
            queryDTO.setEndTime(endTime);
            queryDTO.setPage(page);
            queryDTO.setSize(size);
            Page<ErrorLogDTO> errorLogs = errorLogService.getErrorLogs(queryDTO);
            return ResponseEntity.ok(errorLogs);
        } catch (Exception e) {
            throw new RuntimeException("根据时间范围查询错误日志失败: " + e.getMessage());
        }
    }
}