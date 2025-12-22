package com.example.smartcampus.util;

import com.example.smartcampus.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorLogUtil {

    private static ErrorLogService errorLogService;

    @Autowired
    public ErrorLogUtil(ErrorLogService errorLogService) {
        ErrorLogUtil.errorLogService = errorLogService;
    }

    /**
     * 快速记录错误日志
     */
    public static void logError(String module, String className, String methodName, Throwable throwable) {
        if (errorLogService != null) {
            errorLogService.logError(module, className, methodName, throwable, "ERROR");
        }
    }

    /**
     * 记录警告日志
     */
    public static void logWarn(String module, String className, String methodName, String message) {
        if (errorLogService != null) {
            Exception warnException = new Exception(message);
            errorLogService.logError(module, className, methodName, warnException, "WARN");
        }
    }

    /**
     * 记录信息日志
     */
    public static void logInfo(String module, String className, String methodName, String message) {
        if (errorLogService != null) {
            Exception infoException = new Exception(message);
            errorLogService.logError(module, className, methodName, infoException, "INFO");
        }
    }
}