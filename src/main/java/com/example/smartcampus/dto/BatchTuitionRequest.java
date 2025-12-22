package com.example.smartcampus.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "批量创建学费请求")
public class BatchTuitionRequest {

    @NotBlank(message = "学期不能为空")
    @Schema(description = "学期", example = "2023-2024学年第一学期")
    private String semester;

    @Schema(description = "学费金额", example = "5000.00")
    private BigDecimal amount;

    @Schema(description = "院系", example = "计算机科学与技术学院")
    private String department;

    @Schema(description = "专业", example = "计算机科学与技术")
    private String major;

    @Schema(description = "年级", example = "3")
    private Integer grade;

    @Schema(description = "班级", example = "计科2101班")
    private String className;

    @Schema(description = "学生ID列表")
    private List<Long> studentIds;

    @Schema(description = "缴费截止日期", example = "2023-09-30T23:59:59")
    private String dueDate;

    @Schema(description = "学费描述", example = "2023-2024学年第一学期学费")
    private String description;

    // 构造函数
    public BatchTuitionRequest() {
    }

    // Getter和Setter方法
    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}