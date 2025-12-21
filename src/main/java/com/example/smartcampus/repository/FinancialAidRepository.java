package com.example.smartcampus.repository;

import com.example.smartcampus.entity.FinancialAid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FinancialAidRepository extends JpaRepository<FinancialAid, Long> {
    
    List<FinancialAid> findByStudentId(Long studentId);
    
    List<FinancialAid> findBySemester(String semester);
    
    List<FinancialAid> findByStatus(Integer status);
    
    List<FinancialAid> findByType(String type);
    
    List<FinancialAid> findByStudentIdAndSemester(Long studentId, String semester);
    
    List<FinancialAid> findByStudentIdAndStatus(Long studentId, Integer status);
    
    @Query("SELECT f FROM FinancialAid f WHERE f.student.department = :department AND f.semester = :semester")
    List<FinancialAid> findByDepartmentAndSemester(@Param("department") String department, @Param("semester") String semester);
    
    @Query("SELECT f FROM FinancialAid f WHERE f.student.major = :major AND f.semester = :semester")
    List<FinancialAid> findByMajorAndSemester(@Param("major") String major, @Param("semester") String semester);
    
    @Query("SELECT f FROM FinancialAid f WHERE f.student.grade = :grade AND f.semester = :semester")
    List<FinancialAid> findByGradeAndSemester(@Param("grade") Integer grade, @Param("semester") String semester);
    
    @Query("SELECT COUNT(f) FROM FinancialAid f WHERE f.semester = :semester AND (:status IS NULL OR f.status = :status)")
    Long countBySemesterAndStatus(@Param("semester") String semester, @Param("status") Integer status);
    
    @Query("SELECT SUM(f.amount) FROM FinancialAid f WHERE f.semester = :semester AND (:status IS NULL OR f.status = :status)")
    BigDecimal sumAmountBySemesterAndStatus(@Param("semester") String semester, @Param("status") Integer status);
    
    @Query("SELECT f.type, COUNT(f), SUM(f.amount) FROM FinancialAid f WHERE f.semester = :semester GROUP BY f.type")
    List<Object[]> getStatisticsByTypeAndSemester(@Param("semester") String semester);
}