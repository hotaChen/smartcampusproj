package com.example.smartcampus.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorLogQueryDTO {
    private String module;
    private String className;
    private String methodName;
    private String errorType;
    private String level;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String username;
    private Integer page = 0;
    private Integer size = 20;
}