package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Tuition;
import com.example.smartcampus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TuitionRepository extends JpaRepository<Tuition, Long> {

    List<Tuition> findByStudent(User student);

    List<Tuition> findByStudentId(Long studentId);

    List<Tuition> findBySemester(String semester);

    List<Tuition> findByStatus(Integer status);

    @Query("SELECT t FROM Tuition t WHERE t.student.id = :studentId AND t.semester = :semester")
    Optional<Tuition> findByStudentIdAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);

    @Query("SELECT t FROM Tuition t WHERE t.dueDate < :currentDate AND t.status != 2")
    List<Tuition> findOverdueTuitions(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT t FROM Tuition t WHERE t.dueDate BETWEEN :startDate AND :endDate")
    List<Tuition> findByDueDateBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Tuition t WHERE t.student.department = :department AND t.semester = :semester")
    List<Tuition> findByDepartmentAndSemester(@Param("department") String department, 
                                              @Param("semester") String semester);

    @Query("SELECT t FROM Tuition t WHERE t.student.major = :major AND t.semester = :semester")
    List<Tuition> findByMajorAndSemester(@Param("major") String major, 
                                        @Param("semester") String semester);

    @Query("SELECT t FROM Tuition t WHERE t.student.grade = :grade AND t.semester = :semester")
    List<Tuition> findByGradeAndSemester(@Param("grade") Integer grade, 
                                        @Param("semester") String semester);

    @Query("SELECT SUM(t.amount) FROM Tuition t WHERE t.semester = :semester")
    Double getTotalAmountBySemester(@Param("semester") String semester);

    @Query("SELECT SUM(t.paidAmount) FROM Tuition t WHERE t.semester = :semester")
    Double getTotalPaidAmountBySemester(@Param("semester") String semester);

    @Query("SELECT COUNT(t) FROM Tuition t WHERE t.status = :status AND t.semester = :semester")
    Long countByStatusAndSemester(@Param("status") Integer status, @Param("semester") String semester);

    @Query("SELECT t FROM Tuition t WHERE t.student.id = :studentId ORDER BY t.semester DESC")
    List<Tuition> findByStudentIdOrderBySemesterDesc(@Param("studentId") Long studentId);
    // TuitionRepository.java
    @Query("SELECT SUM(t.amount) FROM Tuition t")
    Double sumTotalAmount();
}