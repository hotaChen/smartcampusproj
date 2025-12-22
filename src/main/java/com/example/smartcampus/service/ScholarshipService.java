package com.example.smartcampus.service;

import com.example.smartcampus.entity.Scholarship;

import java.util.List;
import java.util.Optional;

public interface ScholarshipService {
    
    Scholarship createScholarship(Scholarship scholarship);
    
    Scholarship updateScholarship(Long id, Scholarship scholarship);
    
    void deleteScholarship(Long id);
    
    Optional<Scholarship> getScholarshipById(Long id);
    
    List<Scholarship> getAllScholarships();
    
    List<Scholarship> getScholarshipsByStudent(Long studentId);
    
    List<Scholarship> getScholarshipsBySemester(String semester);
    
    List<Scholarship> getScholarshipsByStatus(Integer status);
    
    List<Scholarship> getScholarshipsByType(String type);
    
    List<Scholarship> getScholarshipsByStudentAndSemester(Long studentId, String semester);
    
    List<Scholarship> getScholarshipsByDepartmentAndSemester(String department, String semester);
    
    List<Scholarship> getScholarshipsByMajorAndSemester(String major, String semester);
    
    List<Scholarship> getScholarshipsByGradeAndSemester(Integer grade, String semester);
    
    ScholarshipStatistics getScholarshipStatistics(String semester);
    
    DepartmentScholarshipStatistics getDepartmentScholarshipStatistics(String department, String semester);
    
    class ScholarshipStatistics {
        private Long totalCount;
        private Long approvedCount;
        private Long pendingCount;
        private Long rejectedCount;
        private Double totalAmount;
        private Double approvedAmount;
        private List<TypeStatistics> typeStatistics;
        
        // Getters and Setters
        public Long getTotalCount() {
            return totalCount;
        }
        
        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }
        
        public Long getApprovedCount() {
            return approvedCount;
        }
        
        public void setApprovedCount(Long approvedCount) {
            this.approvedCount = approvedCount;
        }
        
        public Long getPendingCount() {
            return pendingCount;
        }
        
        public void setPendingCount(Long pendingCount) {
            this.pendingCount = pendingCount;
        }
        
        public Long getRejectedCount() {
            return rejectedCount;
        }
        
        public void setRejectedCount(Long rejectedCount) {
            this.rejectedCount = rejectedCount;
        }
        
        public Double getTotalAmount() {
            return totalAmount;
        }
        
        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }
        
        public Double getApprovedAmount() {
            return approvedAmount;
        }
        
        public void setApprovedAmount(Double approvedAmount) {
            this.approvedAmount = approvedAmount;
        }
        
        public List<TypeStatistics> getTypeStatistics() {
            return typeStatistics;
        }
        
        public void setTypeStatistics(List<TypeStatistics> typeStatistics) {
            this.typeStatistics = typeStatistics;
        }
    }
    
    class DepartmentScholarshipStatistics {
        private String department;
        private Long totalCount;
        private Long approvedCount;
        private Long pendingCount;
        private Long rejectedCount;
        private Double totalAmount;
        private Double approvedAmount;
        
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
        
        public Long getApprovedCount() {
            return approvedCount;
        }
        
        public void setApprovedCount(Long approvedCount) {
            this.approvedCount = approvedCount;
        }
        
        public Long getPendingCount() {
            return pendingCount;
        }
        
        public void setPendingCount(Long pendingCount) {
            this.pendingCount = pendingCount;
        }
        
        public Long getRejectedCount() {
            return rejectedCount;
        }
        
        public void setRejectedCount(Long rejectedCount) {
            this.rejectedCount = rejectedCount;
        }
        
        public Double getTotalAmount() {
            return totalAmount;
        }
        
        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }
        
        public Double getApprovedAmount() {
            return approvedAmount;
        }
        
        public void setApprovedAmount(Double approvedAmount) {
            this.approvedAmount = approvedAmount;
        }
    }
    
    class TypeStatistics {
        private String type;
        private Long count;
        private Double amount;
        
        // Getters and Setters
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
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