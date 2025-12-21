package com.example.smartcampus.controller;

import com.example.smartcampus.entity.Appointment;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /**
     * 学生创建预约
     */
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "创建预约", description = "学生创建新的服务预约")
    public Appointment createAppointment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String serviceType,
            @RequestParam String appointmentTime
    ) {
        Long studentId = userDetails.getUserId();
        return appointmentService.createAppointment(
                studentId,
                LocalDateTime.parse(appointmentTime),
                serviceType
        );
    }

    /**
     * 学生查询自己的预约
     */
    @GetMapping("/mine")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "查询我的预约", description = "学生查询自己的预约记录")
    public List<Appointment> getMyAppointments(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return appointmentService.getAppointmentsByStudent(userDetails.getUserId());
    }

    /**
     * 学生取消自己的预约
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "取消预约", description = "学生取消自己的预约")
    public Appointment cancel(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        return appointmentService.cancelAppointment(id, userDetails.getUserId());
    }

    /**
     * 管理员查询所有预约
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "查询所有预约", description = "管理员查询所有预约记录")
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    /**
     * 管理员审批预约
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "审批预约", description = "管理员审批待处理的预约")
    public Appointment approve(@PathVariable Long id) {
        return appointmentService.approveAppointment(id);
    }

    /**
     * 管理员完成预约
     */
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "完成预约", description = "管理员标记预约为已完成")
    public Appointment complete(@PathVariable Long id) {
        return appointmentService.completeAppointment(id);
    }
}
