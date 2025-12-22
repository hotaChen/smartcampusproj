package com.example.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemReportDTO {
    private Long id;
    private String reportName;
    private String reportType;
    private String description;
    private LocalDateTime generateTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long generatedById;
    private String generatedByName;
    private String reportFormat;
    private String reportData;
    private String filePath;
    private Long fileSize;
    private String status;
    private LocalDateTime createTime;
}