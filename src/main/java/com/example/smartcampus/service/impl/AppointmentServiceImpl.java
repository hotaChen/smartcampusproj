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

    @Override
    public List<Appointment> getAppointmentsByStudent(Long studentId) {
        return appointmentRepository.findByStudentId(studentId);
    }

    @Override
    public Appointment approveAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.APPROVED.name());
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.COMPLETED.name());
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED.name());
        return appointmentRepository.save(appointment);
    }
}
