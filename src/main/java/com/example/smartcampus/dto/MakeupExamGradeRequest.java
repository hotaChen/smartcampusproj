package com.example.smartcampus.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

@Data
public class MakeupExamGradeRequest {
    
    @NotNull(message = "分数不能为空")
    @Min(value = 0, message = "分数不能小于0")
    @Max(value = 100, message = "分数不能大于100")
    private Float score;
    
    @Size(max = 255, message = "成绩等级长度不能超过255字符")
    private String gradeLevel;
    
    @Size(max = 1000, message = "评语长度不能超过1000字符")
    private String comment;
}