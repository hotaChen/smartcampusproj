package com.example.smartcampus.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportRequest {
    private String reportName;
    private String reportType;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reportFormat;
}