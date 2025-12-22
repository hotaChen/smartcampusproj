package com.example.smartcampus.service;

import com.example.smartcampus.entity.SystemReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SystemReportService {

    // 获取所有报表（分页）
    Page<SystemReport> getAllReports(Pageable pageable);

    // 根据报表类型获取
    List<SystemReport> getByReportType(String reportType);

    // 根据生成者ID获取
    List<SystemReport> getByGeneratedById(Long generatedById);

    // 根据状态获取
    List<SystemReport> getByStatus(String status);

    // 根据生成时间范围获取
    List<SystemReport> getByGenerateTimeBetween(LocalDateTime start, LocalDateTime end);

    // 根据时间范围和类型获取
    List<SystemReport> getByDateRangeAndType(LocalDateTime start, LocalDateTime end, String reportType);

    // 生成报表
    SystemReport generateReport(SystemReport systemReport);

    // 更新报表状态
    SystemReport updateReportStatus(Long id, String status);

    // 获取报表统计信息
    Object getReportStatistics();

    // 获取已完成报表数量
    Long getCompletedReportsCount();

    // 按报表类型统计数量
    List<Object[]> getCountByReportType();

    // 删除报表
    void deleteReport(Long id);
}