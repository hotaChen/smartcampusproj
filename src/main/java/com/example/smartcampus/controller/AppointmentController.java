package com.example.smartcampus.controller;

import com.example.smartcampus.entity.Appointment;
import com.example.smartcampus.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    //创建预约
    @PostMapping
    public Appointment createAppointment(
            @RequestParam Long studentId,
            @RequestParam String serviceType,
            @RequestParam String appointmentTime
    ) {
        return appointmentService.createAppointment(
                studentId,
                LocalDateTime.parse(appointmentTime),
                serviceType
        );
    }

    //查询学生预约
    @GetMapping("/student/{studentId}")
    public List<Appointment> getAppointments(@PathVariable Long studentId) {
        return appointmentService.getAppointmentsByStudent(studentId);
    }

    //审批预约（管理员）
    @PutMapping("/{id}/approve")
    public Appointment approve(@PathVariable Long id) {
        return appointmentService.approveAppointment(id);
    }

    //完成预约
    @PutMapping("/{id}/complete")
    public Appointment complete(@PathVariable Long id) {
        return appointmentService.completeAppointment(id);
    }

    //取消预约
    @PutMapping("/{id}/cancel")
    public Appointment cancel(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }
}

