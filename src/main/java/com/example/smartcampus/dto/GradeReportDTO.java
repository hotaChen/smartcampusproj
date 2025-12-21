package com.example.smartcampus.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GradeReportDTO {
    
    private Long studentId;
    private String studentName;
    private String studentIdNumber;
    private String department;
    private String major;
    private String className;
    private Integer grade;
    private String semester;
    private LocalDateTime generateTime;
    
    // 成绩统计信息
    private Integer totalCourses;       // 总课程数
    private Float totalCredits;         // 总学分
    private Float averageScore;         // 平均分
    private Float averageGPA;           // 平均绩点
    private Integer passedCourses;      // 通过课程数
    private Integer failedCourses;      // 不通过课程数
    private Float passRate;             // 通过率
    
    // 成绩详情列表
    private List<CourseGradeDTO> courseGrades;
    
    @Data
    public static class CourseGradeDTO {
        private String courseCode;
        private String courseName;
        private Integer credit;
        private String examType;
        private Float score;
        private String gradeLevel;
        private Float gpa;
        private String remark;
        private String status;          // 通过/不通过
    }
}