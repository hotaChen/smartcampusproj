package com.example.smartcampus.repository;

import com.example.smartcampus.entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    
    List<PaymentRecord> findByStudentId(Long studentId);
    
    List<PaymentRecord> findBySemester(String semester);
    
    List<PaymentRecord> findByStatus(Integer status);
    
    List<PaymentRecord> findByPaymentType(String paymentType);
    
    List<PaymentRecord> findByPaymentMethod(String paymentMethod);
    
    List<PaymentRecord> findByStudentIdAndSemester(Long studentId, String semester);
    
    List<PaymentRecord> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT p FROM PaymentRecord p WHERE p.student.department = :department AND p.semester = :semester")
    List<PaymentRecord> findByDepartmentAndSemester(@Param("department") String department, @Param("semester") String semester);
    
    @Query("SELECT p FROM PaymentRecord p WHERE p.student.major = :major AND p.semester = :semester")
    List<PaymentRecord> findByMajorAndSemester(@Param("major") String major, @Param("semester") String semester);
    
    @Query("SELECT p FROM PaymentRecord p WHERE p.student.grade = :grade AND p.semester = :semester")
    List<PaymentRecord> findByGradeAndSemester(@Param("grade") Integer grade, @Param("semester") String semester);
    
    @Query("SELECT COUNT(p) FROM PaymentRecord p WHERE p.semester = :semester AND (:status IS NULL OR p.status = :status)")
    Long countBySemesterAndStatus(@Param("semester") String semester, @Param("status") Integer status);
    
    @Query("SELECT SUM(p.amount) FROM PaymentRecord p WHERE p.semester = :semester AND (:status IS NULL OR p.status = :status)")
    BigDecimal sumAmountBySemesterAndStatus(@Param("semester") String semester, @Param("status") Integer status);
    
    @Query("SELECT p.paymentType, COUNT(p), SUM(p.amount) FROM PaymentRecord p WHERE p.semester = :semester GROUP BY p.paymentType")
    List<Object[]> getStatisticsByPaymentTypeAndSemester(@Param("semester") String semester);
    
    @Query("SELECT p.paymentMethod, COUNT(p), SUM(p.amount) FROM PaymentRecord p WHERE p.semester = :semester GROUP BY p.paymentMethod")
    List<Object[]> getStatisticsByPaymentMethodAndSemester(@Param("semester") String semester);
    // PaymentRecordRepository.java
    @Query("SELECT SUM(p.amount) FROM PaymentRecord p")
    Double sumTotalAmount();
    @Query("SELECT p.paymentType, SUM(p.amount), COUNT(p) FROM PaymentRecord p GROUP BY p.paymentType")
    List<Object[]> findPaymentTypeSummary();
}