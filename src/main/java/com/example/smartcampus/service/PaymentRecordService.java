package com.example.smartcampus.service;

import com.example.smartcampus.entity.PaymentRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRecordService {
    
    PaymentRecord createPaymentRecord(PaymentRecord paymentRecord);
    
    PaymentRecord updatePaymentRecord(Long id, PaymentRecord paymentRecord);
    
    void deletePaymentRecord(Long id);
    
    Optional<PaymentRecord> getPaymentRecordById(Long id);
    
    List<PaymentRecord> getAllPaymentRecords();
    
    List<PaymentRecord> getPaymentRecordsByStudent(Long studentId);
    
    /**
     * 根据学号获取学生的所有缴费记录
     * @param studentId 学号
     * @return 缴费记录列表
     */
    List<PaymentRecord> getPaymentRecordsByStudentStudentId(String studentId);
    
    List<PaymentRecord> getPaymentRecordsBySemester(String semester);
    
    List<PaymentRecord> getPaymentRecordsByStatus(Integer status);
    
    List<PaymentRecord> getPaymentRecordsByPaymentType(String paymentType);
    
    List<PaymentRecord> getPaymentRecordsByPaymentMethod(String paymentMethod);
    
    List<PaymentRecord> getPaymentRecordsByStudentAndSemester(Long studentId, String semester);
    
    /**
     * 根据学号和学期获取学生的缴费记录
     * @param studentId 学号
     * @param semester 学期
     * @return 缴费记录列表
     */
    List<PaymentRecord> getPaymentRecordsByStudentStudentIdAndSemester(String studentId, String semester);
    
    List<PaymentRecord> getPaymentRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<PaymentRecord> getPaymentRecordsByDepartmentAndSemester(String department, String semester);
    
    List<PaymentRecord> getPaymentRecordsByMajorAndSemester(String major, String semester);
    
    List<PaymentRecord> getPaymentRecordsByGradeAndSemester(Integer grade, String semester);
    
    PaymentRecordStatistics getPaymentRecordStatistics(String semester);
    
    DepartmentPaymentRecordStatistics getDepartmentPaymentRecordStatistics(String department, String semester);
    
    class PaymentRecordStatistics {
        private Long totalCount;
        private Long successCount;
        private Long failedCount;
        private Long pendingCount;
        private Double totalAmount;
        private Double successAmount;
        private List<PaymentTypeStatistics> paymentTypeStatistics;
        private List<PaymentMethodStatistics> paymentMethodStatistics;
        
        // Getters and Setters
        public Long getTotalCount() {
            return totalCount;
        }
        
        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }
        
        public Long getSuccessCount() {
            return successCount;
        }
        
        public void setSuccessCount(Long successCount) {
            this.successCount = successCount;
        }
        
        public Long getFailedCount() {
            return failedCount;
        }
        
        public void setFailedCount(Long failedCount) {
            this.failedCount = failedCount;
        }
        
        public Long getPendingCount() {
            return pendingCount;
        }
        
        public void setPendingCount(Long pendingCount) {
            this.pendingCount = pendingCount;
        }
        
        public Double getTotalAmount() {
            return totalAmount;
        }
        
        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }
        
        public Double getSuccessAmount() {
            return successAmount;
        }
        
        public void setSuccessAmount(Double successAmount) {
            this.successAmount = successAmount;
        }
        
        public List<PaymentTypeStatistics> getPaymentTypeStatistics() {
            return paymentTypeStatistics;
        }
        
        public void setPaymentTypeStatistics(List<PaymentTypeStatistics> paymentTypeStatistics) {
            this.paymentTypeStatistics = paymentTypeStatistics;
        }
        
        public List<PaymentMethodStatistics> getPaymentMethodStatistics() {
            return paymentMethodStatistics;
        }
        
        public void setPaymentMethodStatistics(List<PaymentMethodStatistics> paymentMethodStatistics) {
            this.paymentMethodStatistics = paymentMethodStatistics;
        }
    }
    
    class DepartmentPaymentRecordStatistics {
        private String department;
        private Long totalCount;
        private Long successCount;
        private Long failedCount;
        private Long pendingCount;
        private Double totalAmount;
        private Double successAmount;
        
        // Getters and Setters
        public String getDepartment() {
            return department;
        }
        
        public void setDepartment(String department) {
            this.department = department;
        }
        
        public Long getTotalCount() {
            return totalCount;
        }
        
        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }
        
        public Long getSuccessCount() {
            return successCount;
        }
        
        public void setSuccessCount(Long successCount) {
            this.successCount = successCount;
        }
        
        public Long getFailedCount() {
            return failedCount;
        }
        
        public void setFailedCount(Long failedCount) {
            this.failedCount = failedCount;
        }
        
        public Long getPendingCount() {
            return pendingCount;
        }
        
        public void setPendingCount(Long pendingCount) {
            this.pendingCount = pendingCount;
        }
        
        public Double getTotalAmount() {
            return totalAmount;
        }
        
        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }
        
        public Double getSuccessAmount() {
            return successAmount;
        }
        
        public void setSuccessAmount(Double successAmount) {
            this.successAmount = successAmount;
        }
    }
    
    class PaymentTypeStatistics {
        private String paymentType;
        private Long count;
        private Double amount;
        
        // Getters and Setters
        public String getPaymentType() {
            return paymentType;
        }
        
        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }
        
        public Long getCount() {
            return count;
        }
        
        public void setCount(Long count) {
            this.count = count;
        }
        
        public Double getAmount() {
            return amount;
        }
        
        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }
    
    class PaymentMethodStatistics {
        private String paymentMethod;
        private Long count;
        private Double amount;
        
        // Getters and Setters
        public String getPaymentMethod() {
            return paymentMethod;
        }
        
        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
        
        public Long getCount() {
            return count;
        }
        
        public void setCount(Long count) {
            this.count = count;
        }
        
        public Double getAmount() {
            return amount;
        }
        
        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }
}