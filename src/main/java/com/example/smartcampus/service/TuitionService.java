package com.example.smartcampus.service;

import com.example.smartcampus.entity.Tuition;
import com.example.smartcampus.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TuitionService {
    // 创建学费记录
    Tuition createTuition(Tuition tuition);
    
    // 更新学费记录
    Tuition updateTuition(Long id, Tuition tuition);
    
    // 删除学费记录
    void deleteTuition(Long id);
    
    // 根据ID获取学费记录
    Optional<Tuition> getTuitionById(Long id);
    
    // 获取所有学费记录
    List<Tuition> getAllTuitions();
    
    // 根据学生获取学费记录
    List<Tuition> getTuitionsByStudent(Long studentId);
    
    // 根据学期获取学费记录
    List<Tuition> getTuitionsBySemester(String semester);
    
    // 根据状态获取学费记录
    List<Tuition> getTuitionsByStatus(Integer status);
    
    // 根据学生和学期获取学费记录
    Optional<Tuition> getTuitionByStudentAndSemester(Long studentId, String semester);
    
    // 获取逾期未缴的学费记录
    List<Tuition> getOverdueTuitions();
    
    // 缴费
    Tuition makePayment(Long tuitionId, BigDecimal paymentAmount);
    
    // 批量创建学费记录
    List<Tuition> createTuitionsForStudents(List<User> students, BigDecimal amount, String semester, LocalDateTime dueDate);
    
    // 获取学费统计信息
    TuitionStatistics getTuitionStatistics(String semester);
    
    // 获取院系学费统计
    DepartmentTuitionStatistics getDepartmentTuitionStatistics(String department, String semester);
    
    // 内部类用于学费统计
    class TuitionStatistics {
        private String semester;
        private BigDecimal totalAmount;
        private BigDecimal totalPaidAmount;
        private BigDecimal totalUnpaidAmount;
        private Long totalCount;
        private Long paidCount;
        private Long unpaidCount;
        private Long partiallyPaidCount;
        
        // 构造函数、getter和setter方法
        public TuitionStatistics(String semester, BigDecimal totalAmount, BigDecimal totalPaidAmount, 
                                Long totalCount, Long paidCount, Long unpaidCount, Long partiallyPaidCount) {
            this.semester = semester;
            this.totalAmount = totalAmount;
            this.totalPaidAmount = totalPaidAmount;
            this.totalUnpaidAmount = totalAmount.subtract(totalPaidAmount);
            this.totalCount = totalCount;
            this.paidCount = paidCount;
            this.unpaidCount = unpaidCount;
            this.partiallyPaidCount = partiallyPaidCount;
        }
        
        // Getter方法
        public String getSemester() { return semester; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public BigDecimal getTotalPaidAmount() { return totalPaidAmount; }
        public BigDecimal getTotalUnpaidAmount() { return totalUnpaidAmount; }
        public Long getTotalCount() { return totalCount; }
        public Long getPaidCount() { return paidCount; }
        public Long getUnpaidCount() { return unpaidCount; }
        public Long getPartiallyPaidCount() { return partiallyPaidCount; }
    }
    
    // 内部类用于院系学费统计
    class DepartmentTuitionStatistics {
        private String department;
        private String semester;
        private BigDecimal totalAmount;
        private BigDecimal totalPaidAmount;
        private BigDecimal totalUnpaidAmount;
        private Long totalCount;
        private Long paidCount;
        private Long unpaidCount;
        private Long partiallyPaidCount;
        
        // 构造函数、getter和setter方法
        public DepartmentTuitionStatistics(String department, String semester, BigDecimal totalAmount, 
                                         BigDecimal totalPaidAmount, Long totalCount, Long paidCount, 
                                         Long unpaidCount, Long partiallyPaidCount) {
            this.department = department;
            this.semester = semester;
            this.totalAmount = totalAmount;
            this.totalPaidAmount = totalPaidAmount;
            this.totalUnpaidAmount = totalAmount.subtract(totalPaidAmount);
            this.totalCount = totalCount;
            this.paidCount = paidCount;
            this.unpaidCount = unpaidCount;
            this.partiallyPaidCount = partiallyPaidCount;
        }
        
        // Getter方法
        public String getDepartment() { return department; }
        public String getSemester() { return semester; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public BigDecimal getTotalPaidAmount() { return totalPaidAmount; }
        public BigDecimal getTotalUnpaidAmount() { return totalUnpaidAmount; }
        public Long getTotalCount() { return totalCount; }
        public Long getPaidCount() { return paidCount; }
        public Long getUnpaidCount() { return unpaidCount; }
        public Long getPartiallyPaidCount() { return partiallyPaidCount; }
    }
}