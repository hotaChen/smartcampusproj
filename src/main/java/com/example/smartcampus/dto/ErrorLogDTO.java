package com.example.smartcampus.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorLogDTO {
    private Long id;
    private String module;
    private String className;
    private String methodName;
    private String errorMessage;
    private String stackTrace;
    private String errorType;
    private String level;
    private String requestUrl;
    private String requestMethod;
    private String clientIp;
    private String requestParams;
    private LocalDateTime errorTime;
    private Long userId;
    private String username;
    private LocalDateTime createTime;
}