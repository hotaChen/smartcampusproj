package com.example.smartcampus.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "学费响应")
public class TuitionResponse {

    @Schema(description = "学费ID", example = "1")
    private Long id;

    @Schema(description = "学生ID", example = "1")
    private Long studentId;

    @Schema(description = "学生姓名", example = "张三")
    private String studentName;

    @Schema(description = "学号", example = "2021001")
    private String studentNumber;

    @Schema(description = "学费金额", example = "5000.00")
    private BigDecimal amount;

    @Schema(description = "已缴金额", example = "2000.00")
    private BigDecimal paidAmount;

    @Schema(description = "未缴金额", example = "3000.00")
    private BigDecimal unpaidAmount;

    @Schema(description = "学期", example = "2023-2024学年第一学期")
    private String semester;

    @Schema(description = "缴费截止日期", example = "2023-09-30T23:59:59")
    private LocalDateTime dueDate;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "状态描述", example = "部分缴费")
    private String statusDescription;

    @Schema(description = "学费描述", example = "2023-2024学年第一学期学费")
    private String description;

    @Schema(description = "创建时间", example = "2023-08-01T10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2023-08-15T14:30:00")
    private LocalDateTime updateTime;

    // 构造函数
    public TuitionResponse() {
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(BigDecimal unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}