package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.SystemReport;
import com.example.smartcampus.repository.SystemReportRepository;
import com.example.smartcampus.service.SystemReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SystemReportServiceImpl implements SystemReportService {

    private final SystemReportRepository systemReportRepository;

    public SystemReportServiceImpl(SystemReportRepository systemReportRepository) {
        this.systemReportRepository = systemReportRepository;
    }

    @Override
    public Page<SystemReport> getAllReports(Pageable pageable) {
        return systemReportRepository.findByOrderByGenerateTimeDesc(pageable);
    }

    @Override
    public List<SystemReport> getByReportType(String reportType) {
        return systemReportRepository.findByReportTypeOrderByGenerateTimeDesc(reportType);
    }

    @Override
    public List<SystemReport> getByGeneratedById(Long generatedById) {
        return systemReportRepository.findByGeneratedByIdOrderByGenerateTimeDesc(generatedById);
    }

    @Override
    public List<SystemReport> getByStatus(String status) {
        return systemReportRepository.findByStatusOrderByGenerateTimeDesc(status);
    }

    @Override
    public List<SystemReport> getByGenerateTimeBetween(LocalDateTime start, LocalDateTime end) {
        return systemReportRepository.findByGenerateTimeBetween(start, end);
    }

    @Override
    public List<SystemReport> getByDateRangeAndType(LocalDateTime start, LocalDateTime end, String reportType) {
        return systemReportRepository.findByDateRangeAndType(start, end, reportType);
    }

    @Override
    public SystemReport generateReport(SystemReport systemReport) {
        // 设置默认值
        if (systemReport.getGenerateTime() == null) {
            systemReport.setGenerateTime(LocalDateTime.now());
        }
        if (systemReport.getStatus() == null) {
            systemReport.setStatus("GENERATING");
        }
        if (systemReport.getReportFormat() == null) {
            systemReport.setReportFormat("PDF");
        }

        // 保存报表
        SystemReport savedReport = systemReportRepository.save(systemReport);

        // 这里可以添加报表生成的业务逻辑
        // 比如调用报表生成服务，更新状态等

        return savedReport;
    }

    @Override
    public SystemReport updateReportStatus(Long id, String status) {
        SystemReport report = systemReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("报表不存在，ID: " + id));
        report.setStatus(status);
        return systemReportRepository.save(report);
    }

    @Override
    public Object getReportStatistics() {
        // 返回统计信息
        return new Object() {
            public final Long totalReports = systemReportRepository.count();
            public final Long completedReports = systemReportRepository.countCompletedReports();
        };
    }

    @Override
    public Long getCompletedReportsCount() {
        return systemReportRepository.countCompletedReports();
    }

    @Override
    public List<Object[]> getCountByReportType() {
        return systemReportRepository.countByReportType();
    }

    @Override
    public void deleteReport(Long id) {
        if (!systemReportRepository.existsById(id)) {
            throw new RuntimeException("报表不存在，ID: " + id);
        }
        systemReportRepository.deleteById(id);
    }
}