package com.example.smartcampus.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "学费创建请求")
public class TuitionRequest {

    @Schema(description = "学生ID（已废弃，请使用studentNumber）", example = "1", hidden = true)
    private Long studentId;

    @Schema(description = "学生学号（推荐使用此字段）", example = "20230001")
    private String studentNumber;

    @NotNull(message = "学费金额不能为空")
    @DecimalMin(value = "0.01", message = "学费金额必须大于0")
    @Schema(description = "学费金额", example = "5000.00")
    private BigDecimal amount;

    @NotBlank(message = "学期不能为空")
    @Schema(description = "学期", example = "2023-2024学年第一学期")
    private String semester;

    @NotNull(message = "缴费截止日期不能为空")
    @Schema(description = "缴费截止日期", example = "2023-09-30T23:59:59")
    private LocalDateTime dueDate;

    @Schema(description = "学费描述", example = "2023-2024学年第一学期学费")
    private String description;

    // 构造函数
    public TuitionRequest() {
    }

    public TuitionRequest(Long studentId, BigDecimal amount, String semester, LocalDateTime dueDate) {
        this.studentId = studentId;
        this.amount = amount;
        this.semester = semester;
        this.dueDate = dueDate;
    }

    // Getter和Setter方法
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}