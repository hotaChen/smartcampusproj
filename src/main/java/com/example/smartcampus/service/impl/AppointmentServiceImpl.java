package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.Appointment;
import com.example.smartcampus.entity.AppointmentStatus;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.AppointmentRepository;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * 学生创建预约
     */
    @Override
    public Appointment createAppointment(Long studentId,
                                         LocalDateTime appointmentTime,
                                         String serviceType) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Appointment appointment = new Appointment();
        appointment.setStudent(student);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setServiceType(serviceType);
        appointment.setStatus(AppointmentStatus.PENDING.name());

        return appointmentRepository.save(appointment);
    }

    /**
     * 学生查询自己的预约
     */
    @Override
    public List<Appointment> getAppointmentsByStudent(Long studentId) {
        return appointmentRepository.findByStudentId(studentId);
    }

    /**
     * 管理员查询所有预约
     */
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * 管理员审批预约
     */
    @Override
    public Appointment approveAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!AppointmentStatus.PENDING.name().equals(appointment.getStatus())) {
            throw new RuntimeException("只有待审批的预约才能被审批");
        }

        appointment.setStatus(AppointmentStatus.APPROVED.name());
        return appointmentRepository.save(appointment);
    }

    /**
     * 管理员完成预约
     */
    @Override
    public Appointment completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!AppointmentStatus.APPROVED.name().equals(appointment.getStatus())) {
            throw new RuntimeException("只有已审批的预约才能完成");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED.name());
        return appointmentRepository.save(appointment);
    }

    /**
     * 学生取消自己的预约
     */
    @Override
    public Appointment cancelAppointment(Long appointmentId, Long studentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // 1️⃣ 校验是不是自己的预约
        if (!appointment.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("不能取消他人的预约");
        }

        // 2️⃣ 校验状态是否合法
        if (AppointmentStatus.COMPLETED.name().equals(appointment.getStatus())) {
            throw new RuntimeException("已完成的预约不能取消");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED.name());
        return appointmentRepository.save(appointment);
    }
}

