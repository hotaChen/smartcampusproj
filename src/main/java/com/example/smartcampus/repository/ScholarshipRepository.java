package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
    
    List<Scholarship> findByStudentId(Long studentId);
    
    List<Scholarship> findBySemester(String semester);
    
    List<Scholarship> findByStatus(Integer status);
    
    List<Scholarship> findByType(String type);
    
    List<Scholarship> findByStudentIdAndSemester(Long studentId, String semester);
    
    List<Scholarship> findByStudentIdAndStatus(Long studentId, Integer status);
    
    @Query("SELECT s FROM Scholarship s WHERE s.student.department = :department AND s.semester = :semester")
    List<Scholarship> findByDepartmentAndSemester(@Param("department") String department, @Param("semester") String semester);
    
    @Query("SELECT s FROM Scholarship s WHERE s.student.major = :major AND s.semester = :semester")
    List<Scholarship> findByMajorAndSemester(@Param("major") String major, @Param("semester") String semester);
    
    @Query("SELECT s FROM Scholarship s WHERE s.student.grade = :grade AND s.semester = :semester")
    List<Scholarship> findByGradeAndSemester(@Param("grade") Integer grade, @Param("semester") String semester);
    
    @Query("SELECT COUNT(s) FROM Scholarship s WHERE s.semester = :semester AND (:status IS NULL OR s.status = :status)")
    Long countBySemesterAndStatus(@Param("semester") String semester, @Param("status") Integer status);
    
    @Query("SELECT SUM(s.amount) FROM Scholarship s WHERE s.semester = :semester AND (:status IS NULL OR s.status = :status)")
    BigDecimal sumAmountBySemesterAndStatus(@Param("semester") String semester, @Param("status") Integer status);
    
    @Query("SELECT s.type, COUNT(s), SUM(s.amount) FROM Scholarship s WHERE s.semester = :semester GROUP BY s.type")
    List<Object[]> getStatisticsByTypeAndSemester(@Param("semester") String semester);
}