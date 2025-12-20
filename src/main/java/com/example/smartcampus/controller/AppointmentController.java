package com.example.smartcampus.controller;

import com.example.smartcampus.entity.Appointment;
import com.example.smartcampus.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "预约管理", description = "校园服务预约相关接口")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @Operation(summary = "创建预约", description = "学生创建新的服务预约")
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

    @GetMapping("/student/{studentId}")
    @Operation(summary = "查询学生预约", description = "查询指定学生的所有预约记录")
    public List<Appointment> getAppointments(@PathVariable Long studentId) {
        return appointmentService.getAppointmentsByStudent(studentId);
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "审批预约", description = "管理员审批待处理的预约")
    public Appointment approve(@PathVariable Long id) {
        return appointmentService.approveAppointment(id);
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "完成预约", description = "管理员标记预约为已完成")
    public Appointment complete(@PathVariable Long id) {
        return appointmentService.completeAppointment(id);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消预约", description = "学生取消自己的预约")
    public Appointment cancel(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }
}