package com.example.smartcampus.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FinancialAidRequest {
    private Long studentId;
    private BigDecimal amount;
    private String name;
    private String type;
    private String semester;
    private LocalDateTime awardDate;
    private String description;
    private String reason;
    private Integer status;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public LocalDateTime getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(LocalDateTime awardDate) {
        this.awardDate = awardDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}