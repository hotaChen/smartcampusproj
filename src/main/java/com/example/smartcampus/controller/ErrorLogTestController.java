package com.example.smartcampus.controller;

import com.example.smartcampus.util.ErrorLogUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/error-log")
public class ErrorLogTestController {

    /**
     * 测试错误日志记录 - 模拟空指针异常（仅管理员可访问）
     */
    @GetMapping("/null-pointer")
    @PreAuthorize("hasRole('ADMIN')")
    public String testNullPointer() {
        try {
            String str = null;
            return str.length() + ""; // 这里会抛出空指针异常
        } catch (Exception e) {
            ErrorLogUtil.logError("测试模块", "ErrorLogTestController", "testNullPointer", e);
            return "空指针异常已记录到错误日志";
        }
    }

    /**
     * 测试错误日志记录 - 模拟数组越界异常（仅管理员可访问）
     */
    @GetMapping("/array-index")
    @PreAuthorize("hasRole('ADMIN')")
    public String testArrayIndex() {
        try {
            int[] arr = new int[5];
            return "数组元素: " + arr[10]; // 这里会抛出数组越界异常
        } catch (Exception e) {
            ErrorLogUtil.logError("测试模块", "ErrorLogTestController", "testArrayIndex", e);
            return "数组越界异常已记录到错误日志";
        }
    }

    /**
     * 测试手动记录警告日志（仅管理员可访问）
     */
    @GetMapping("/warn")
    @PreAuthorize("hasRole('ADMIN')")
    public String testWarnLog() {
        ErrorLogUtil.logWarn("测试模块", "ErrorLogTestController", "testWarnLog",
                "这是一个测试警告信息");
        return "警告日志已记录";
    }

    /**
     * 测试手动记录信息日志（仅管理员可访问）
     */
    @GetMapping("/info")
    @PreAuthorize("hasRole('ADMIN')")
    public String testInfoLog() {
        ErrorLogUtil.logInfo("测试模块", "ErrorLogTestController", "testInfoLog",
                "这是一个测试信息日志");
        return "信息日志已记录";
    }

    /**
     * 测试未捕获异常（会被AOP自动捕获）- 仅管理员可访问
     */
    @GetMapping("/uncaught")
    @PreAuthorize("hasRole('ADMIN')")
    public String testUncaughtException() {
        // 这个异常会被AOP切面自动捕获并记录
        throw new RuntimeException("这是一个未捕获的测试异常");
    }
}