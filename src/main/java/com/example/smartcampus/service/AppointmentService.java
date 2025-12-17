package com.example.smartcampus.service;

import com.example.smartcampus.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    // 学生创建预约
    Appointment createAppointment(
            Long studentId,
            LocalDateTime appointmentTime,
            String serviceType
    );

    // 查询学生的预约
    List<Appointment> getAppointmentsByStudent(Long studentId);

    // 管理员审批预约
    Appointment approveAppointment(Long appointmentId);

    // 管理员完成预约
    Appointment completeAppointment(Long appointmentId);

    // 学生取消预约
    Appointment cancelAppointment(Long appointmentId);
}
