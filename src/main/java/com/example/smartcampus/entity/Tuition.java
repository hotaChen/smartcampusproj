package com.example.smartcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tuitions")
public class Tuition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private Integer status = 0; // 状态：0-未缴费，1-部分缴费，2-已缴费

    private String description;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

    // 构造函数
    public Tuition() {}

    public Tuition(User student, BigDecimal amount, String semester, LocalDateTime dueDate) {
        this.student = student;
        this.amount = amount;
        this.semester = semester;
        this.dueDate = dueDate;
    }

    // 计算未缴费金额
    public BigDecimal getUnpaidAmount() {
        return amount.subtract(paidAmount);
    }

    // 更新缴费状态
    public void updatePaymentStatus() {
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.status = 0; // 未缴费
        } else if (paidAmount.compareTo(amount) < 0) {
            this.status = 1; // 部分缴费
        } else {
            this.status = 2; // 已缴费
        }
        this.updateTime = LocalDateTime.now();
    }

    // 添加缴费金额
    public void addPayment(BigDecimal paymentAmount) {
        this.paidAmount = this.paidAmount.add(paymentAmount);
        updatePaymentStatus();
    }
}