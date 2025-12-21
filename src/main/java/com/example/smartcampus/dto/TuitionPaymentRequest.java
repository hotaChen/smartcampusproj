package com.example.smartcampus.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "学费缴费请求")
public class TuitionPaymentRequest {

    @NotNull(message = "学费ID不能为空")
    @Schema(description = "学费ID", example = "1")
    private Long tuitionId;

    @NotNull(message = "缴费金额不能为空")
    @DecimalMin(value = "0.01", message = "缴费金额必须大于0")
    @Schema(description = "缴费金额", example = "1000.00")
    private BigDecimal paymentAmount;

    @Schema(description = "缴费方式", example = "银行卡")
    private String paymentMethod;

    @Schema(description = "缴费描述", example = "第一次缴费")
    private String description;

    // 构造函数
    public TuitionPaymentRequest() {
    }

    public TuitionPaymentRequest(Long tuitionId, BigDecimal paymentAmount) {
        this.tuitionId = tuitionId;
        this.paymentAmount = paymentAmount;
    }

    // Getter和Setter方法
    public Long getTuitionId() {
        return tuitionId;
    }

    public void setTuitionId(Long tuitionId) {
        this.tuitionId = tuitionId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}