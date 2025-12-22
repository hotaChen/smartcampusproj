package com.example.smartcampus.controller;

import com.example.smartcampus.entity.SystemReport;
import com.example.smartcampus.service.SystemReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/system-reports")
@CrossOrigin(origins = "*")
public class SystemReportController {

    private final SystemReportService systemReportService;

    public SystemReportController(SystemReportService systemReportService) {
        this.systemReportService = systemReportService;
    }

    // 获取所有报表（分页，按生成时间降序）
    @GetMapping
    public ResponseEntity<Page<SystemReport>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SystemReport> reports = systemReportService.getAllReports(pageable);
        return ResponseEntity.ok(reports);
    }

    // 根据报表类型获取
    @GetMapping("/type/{reportType}")
    public ResponseEntity<List<SystemReport>> getReportsByType(@PathVariable String reportType) {
        List<SystemReport> reports = systemReportService.getByReportType(reportType);
        return ResponseEntity.ok(reports);
    }

    // 根据生成者ID获取
    @GetMapping("/generated-by/{generatedById}")
    public ResponseEntity<List<SystemReport>> getReportsByGenerator(@PathVariable Long generatedById) {
        List<SystemReport> reports = systemReportService.getByGeneratedById(generatedById);
        return ResponseEntity.ok(reports);
    }

    // 根据状态获取
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SystemReport>> getReportsByStatus(@PathVariable String status) {
        List<SystemReport> reports = systemReportService.getByStatus(status);
        return ResponseEntity.ok(reports);
    }

    // 根据时间范围获取
    @GetMapping("/time-range")
    public ResponseEntity<List<SystemReport>> getReportsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<SystemReport> reports = systemReportService.getByGenerateTimeBetween(start, end);
        return ResponseEntity.ok(reports);
    }

    // 根据时间范围和类型获取
    @GetMapping("/time-range-type")
    public ResponseEntity<List<SystemReport>> getReportsByDateRangeAndType(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String reportType) {
        List<SystemReport> reports = systemReportService.getByDateRangeAndType(start, end, reportType);
        return ResponseEntity.ok(reports);
    }

    // 生成新报表
    @PostMapping("/generate")
    public ResponseEntity<SystemReport> generateReport(@RequestBody SystemReport systemReport) {
        SystemReport generatedReport = systemReportService.generateReport(systemReport);
        return ResponseEntity.ok(generatedReport);
    }

    // 更新报表状态
    @PutMapping("/{id}/status")
    public ResponseEntity<SystemReport> updateReportStatus(
            @PathVariable Long id, @RequestParam String status) {
        SystemReport updatedReport = systemReportService.updateReportStatus(id, status);
        return ResponseEntity.ok(updatedReport);
    }

    // 获取报表统计信息
    @GetMapping("/statistics")
    public ResponseEntity<?> getReportStatistics() {
        return ResponseEntity.ok(systemReportService.getReportStatistics());
    }

    // 获取已完成报表数量
    @GetMapping("/count/completed")
    public ResponseEntity<Long> getCompletedReportsCount() {
        Long count = systemReportService.getCompletedReportsCount();
        return ResponseEntity.ok(count);
    }

    // 按报表类型统计数量
    @GetMapping("/count/by-type")
    public ResponseEntity<List<Object[]>> getCountByReportType() {
        List<Object[]> statistics = systemReportService.getCountByReportType();
        return ResponseEntity.ok(statistics);
    }

    // 下载报表文件
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        // 这里需要实现文件下载逻辑
        return ResponseEntity.notFound().build();
    }

    // 删除报表
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        systemReportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }
}