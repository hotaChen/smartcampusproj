package com.example.smartcampus.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class MakeupExamScheduleRequest {
    
    @NotBlank(message = "学号不能为空")
    private String studentId;
    
    @NotBlank(message = "课程代码不能为空")
    private String courseCode;
    
    private String courseName;
    
    private Double originalScore;
    
    @NotNull(message = "补考日期不能为空")
    private java.time.LocalDateTime makeupDate;
    
    @NotBlank(message = "补考地点不能为空")
    private String makeupLocation;
    
    private String semester;
    
    private java.time.LocalDateTime examDate;
    
    private String examLocation;
    
    private String originalGrade;
    
    private Long originalGradeId;
    
    private Long teacherId;
}
