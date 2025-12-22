package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByStudentId(Long studentId);
    
    /**
     * 根据学号查找预约记录
     */
    @Query("SELECT a FROM Appointment a WHERE a.student.studentId = :studentId")
    List<Appointment> findByStudentStudentId(@Param("studentId") String studentId);
}
