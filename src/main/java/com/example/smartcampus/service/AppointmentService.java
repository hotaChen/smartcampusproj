package com.example.smartcampus.service;

import com.example.smartcampus.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    Appointment createAppointment(Long studentId,
                                  LocalDateTime appointmentTime,
                                  String serviceType);

    List<Appointment> getAppointmentsByStudent(Long studentId);
    
    /**
     * 根据学号获取学生的所有预约记录
     * @param studentId 学号
     * @return 预约记录列表
     */
    List<Appointment> getAppointmentsByStudentStudentId(String studentId);

    List<Appointment> getAllAppointments();

    Appointment approveAppointment(Long appointmentId);

    Appointment completeAppointment(Long appointmentId);

    Appointment cancelAppointment(Long appointmentId, Long studentId);
}
