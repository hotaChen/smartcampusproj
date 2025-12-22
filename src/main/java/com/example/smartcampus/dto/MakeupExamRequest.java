package com.example.smartcampus.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class MakeupExamRequest {
    
    @NotNull(message = "原始成绩ID不能为空")
    private Long originalGradeId;
    
    @NotBlank(message = "课程代码不能为空")
    private String courseCode;
    
    @NotBlank(message = "课程名称不能为空")
    private String courseName;
    
    @NotNull(message = "考试日期不能为空")
    @Future(message = "考试日期必须是未来时间")
    private LocalDateTime examDate;
    
    @NotBlank(message = "考试地点不能为空")
    private String examLocation;
    
    @NotBlank(message = "学期不能为空")
    private String semester;
    
    @Size(max = 1000, message = "申请原因长度不能超过1000字符")
    private String applyReason;
}