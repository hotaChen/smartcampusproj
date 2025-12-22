package com.example.smartcampus.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
public class GradeEntryRequest {

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @NotBlank(message = "课程代码不能为空")
    private String courseCode;

    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    @NotBlank(message = "考试类型不能为空")
    private String examType; // 期中, 期末, 补考

    @NotNull(message = "分数不能为空")
    @Min(value = 0, message = "分数不能小于0")
    @Max(value = 100, message = "分数不能大于100")
    private Float score;

    @NotBlank(message = "学期不能为空")
    private String semester; // 例如: 2023-2024-1

    private Integer credit; // 学分

    private String gradeLevel; // 成绩等级: A, B, C, D, F

    private Float finalScore; // 期末成绩

    private Float midtermScore; // 期中成绩

    private Float regularScore; // 平时成绩

    private String remark; // 备注

    private Float totalScore; // 总成绩

    private Float grade; // 绩点

    private String comment; // 评语
}