package com.example.smartcampus.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ReportDataDTO {
    private String reportType;
    private LocalDateTime generateTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Map<String, Object> summary;
    private List<Map<String, Object>> details;
    private Map<String, Object> statistics;

    // 学术报表数据
    private AcademicReportData academicData;

    // 财务报表数据
    private FinancialReportData financialData;

    // 用户报表数据
    private UserReportData userData;

    @Data
    public static class AcademicReportData {
        private Integer totalCourses;
        private Integer totalStudents;
        private Integer totalTeachers;
        private Double averageGrade;
        private Integer passRate;
        private List<CourseGradeSummary> courseGrades;
    }

    @Data
    public static class FinancialReportData {
        private Double totalTuition;
        private Double totalScholarships;
        private Double totalFinancialAid;
        private Double totalPayments;
        private List<PaymentSummary> paymentSummary;
    }

    @Data
    public static class UserReportData {
        private Integer totalUsers;
        private Integer activeUsers;
        private Map<String, Integer> userTypeDistribution;
        private Map<String, Integer> departmentDistribution;
    }

    @Data
    public static class CourseGradeSummary {
        private String courseName;
        private Double averageScore;
        private Integer totalStudents;
        private Integer passCount;
    }

    @Data
    public static class PaymentSummary {
        private String paymentType;
        private Double totalAmount;
        private Integer transactionCount;
    }
}