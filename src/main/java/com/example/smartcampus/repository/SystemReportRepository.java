package com.example.smartcampus.repository;

import com.example.smartcampus.entity.SystemReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemReportRepository extends JpaRepository<SystemReport, Long> {

    Page<SystemReport> findByOrderByGenerateTimeDesc(Pageable pageable);

    List<SystemReport> findByReportTypeOrderByGenerateTimeDesc(String reportType);

    List<SystemReport> findByGeneratedByIdOrderByGenerateTimeDesc(Long generatedById);

    List<SystemReport> findByGenerateTimeBetween(LocalDateTime start, LocalDateTime end);

    List<SystemReport> findByStatusOrderByGenerateTimeDesc(String status);

    @Query("SELECT sr FROM SystemReport sr WHERE sr.generateTime BETWEEN :startDate AND :endDate " +
            "AND sr.reportType = :reportType ORDER BY sr.generateTime DESC")
    List<SystemReport> findByDateRangeAndType(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("reportType") String reportType);

    @Query("SELECT COUNT(sr) FROM SystemReport sr WHERE sr.status = 'COMPLETED'")
    Long countCompletedReports();

    @Query("SELECT sr.reportType, COUNT(sr) FROM SystemReport sr GROUP BY sr.reportType")
    List<Object[]> countByReportType();
}