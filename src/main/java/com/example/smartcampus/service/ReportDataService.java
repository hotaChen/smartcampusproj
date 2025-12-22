package com.example.smartcampus.service;

import com.example.smartcampus.dto.ReportDataDTO;

import java.time.LocalDateTime;

public interface ReportDataService {

    /**
     * 生成报表数据
     */
    ReportDataDTO generateReportData(String reportType, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 生成学术报表数据
     */
    ReportDataDTO.AcademicReportData generateAcademicReportData(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 生成财务报表数据
     */
    ReportDataDTO.FinancialReportData generateFinancialReportData(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 生成用户报表数据
     */
    ReportDataDTO.UserReportData generateUserReportData(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 生成课程报表数据
     */
    ReportDataDTO.AcademicReportData generateCourseReportData(LocalDateTime startDate, LocalDateTime endDate);
}