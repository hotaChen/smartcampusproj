package com.example.smartcampus.controller;

import com.example.smartcampus.dto.BatchTuitionRequest;
import com.example.smartcampus.dto.TuitionPaymentRequest;
import com.example.smartcampus.dto.TuitionRequest;
import com.example.smartcampus.dto.TuitionResponse;
import com.example.smartcampus.entity.Tuition;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.service.TuitionService;
import com.example.smartcampus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tuitions")
@Tag(name = "学费管理", description = "学费信息管理相关接口")
public class TuitionController {

    private static final Logger logger = LoggerFactory.getLogger(TuitionController.class);

    private final TuitionService tuitionService;
    private final UserService userService;

    public TuitionController(TuitionService tuitionService, UserService userService) {
        this.tuitionService = tuitionService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "创建学费记录", description = "为学生创建学费记录")
    public ResponseEntity<TuitionResponse> createTuition(@RequestBody TuitionRequest request) {
        logger.info("=== TuitionController.createTuition() 被调用 ===");
        logger.info("创建学费记录: 学号={}, 学期={}, 金额={}", 
                request.getStudentNumber(), request.getSemester(), request.getAmount());

        try {
            // 获取学生信息（优先使用studentNumber）
            Optional<User> studentOpt = userService.getUserByStudentId(request.getStudentNumber());
            if (!studentOpt.isPresent() && request.getStudentId() != null) {
                // 兼容旧接口，使用studentId获取
                studentOpt = Optional.ofNullable(userService.getUserById(request.getStudentId()));
            }
            
            if (!studentOpt.isPresent()) {
                logger.warn("未找到学生: 学号={}", request.getStudentNumber());
                return ResponseEntity.badRequest().build();
            }
            User student = studentOpt.get();
            
            // 创建学费记录
            Tuition tuition = new Tuition();
            tuition.setStudent(student);
            tuition.setAmount(request.getAmount());
            tuition.setSemester(request.getSemester());
            tuition.setDueDate(request.getDueDate());
            tuition.setDescription(request.getDescription());
            
            Tuition savedTuition = tuitionService.createTuition(tuition);
            
            // 转换为响应DTO
            TuitionResponse response = convertToResponse(savedTuition);
            
            logger.info("✅ 成功创建学费记录: ID={}", savedTuition.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("创建学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "批量创建学费记录", description = "为多个学生批量创建学费记录")
    public ResponseEntity<List<TuitionResponse>> createBatchTuitions(@RequestBody BatchTuitionRequest request) {
        logger.info("=== TuitionController.createBatchTuitions() 被调用 ===");
        logger.info("批量创建学费记录: 学期={}, 金额={}", request.getSemester(), request.getAmount());

        try {
            List<User> students;
            
            // 根据条件筛选学生
            if (request.getStudentIds() != null && !request.getStudentIds().isEmpty()) {
                // 根据学生ID列表获取学生
                students = request.getStudentIds().stream()
                        .map(userService::getUserById)
                        .collect(Collectors.toList());
            } else {
                // 根据院系、专业、年级、班级等条件获取学生
                students = userService.getAllUsers().stream()
                        .filter(user -> "STUDENT".equals(user.getUserType()))
                        .filter(user -> request.getDepartment() == null || 
                                request.getDepartment().equals(user.getDepartment()))
                        .filter(user -> request.getMajor() == null || 
                                request.getMajor().equals(user.getMajor()))
                        .filter(user -> request.getGrade() == null || 
                                request.getGrade().equals(user.getGrade()))
                        .filter(user -> request.getClassName() == null || 
                                request.getClassName().equals(user.getClassName()))
                        .collect(Collectors.toList());
            }
            
            // 解析截止日期
            LocalDateTime dueDate = LocalDateTime.parse(request.getDueDate());
            
            // 批量创建学费记录
            List<Tuition> tuitions = tuitionService.createTuitionsForStudents(
                    students, request.getAmount(), request.getSemester(), dueDate);
            
            // 转换为响应DTO
            List<TuitionResponse> responses = tuitions.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功批量创建 {} 条学费记录", responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("批量创建学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新学费记录", description = "更新学费记录信息")
    public ResponseEntity<TuitionResponse> updateTuition(@PathVariable Long id, @RequestBody TuitionRequest request) {
        logger.info("=== TuitionController.updateTuition() 被调用 ===");
        logger.info("更新学费记录: ID={}", id);

        try {
            // 获取现有学费记录
            Optional<Tuition> tuitionOpt = tuitionService.getTuitionById(id);
            if (!tuitionOpt.isPresent()) {
                logger.warn("未找到学费记录: ID={}", id);
                return ResponseEntity.notFound().build();
            }
            
            Tuition existingTuition = tuitionOpt.get();
            
            // 更新字段
            if (request.getAmount() != null) {
                existingTuition.setAmount(request.getAmount());
            }
            if (request.getSemester() != null) {
                existingTuition.setSemester(request.getSemester());
            }
            if (request.getDueDate() != null) {
                existingTuition.setDueDate(request.getDueDate());
            }
            if (request.getDescription() != null) {
                existingTuition.setDescription(request.getDescription());
            }
            
            Tuition updatedTuition = tuitionService.updateTuition(id, existingTuition);
            
            // 转换为响应DTO
            TuitionResponse response = convertToResponse(updatedTuition);
            
            logger.info("✅ 成功更新学费记录: ID={}", updatedTuition.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("更新学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除学费记录", description = "删除学费记录")
    public ResponseEntity<Void> deleteTuition(@PathVariable Long id) {
        logger.info("=== TuitionController.deleteTuition() 被调用 ===");
        logger.info("删除学费记录: ID={}", id);

        try {
            tuitionService.deleteTuition(id);
            logger.info("✅ 成功删除学费记录: ID={}", id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("删除学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "根据ID获取学费记录", description = "根据ID获取学费记录详情")
    public ResponseEntity<TuitionResponse> getTuitionById(@PathVariable Long id) {
        logger.info("=== TuitionController.getTuitionById() 被调用 ===");
        logger.info("查询学费记录: ID={}", id);

        try {
            Optional<Tuition> tuitionOpt = tuitionService.getTuitionById(id);
            
            if (tuitionOpt.isPresent()) {
                TuitionResponse response = convertToResponse(tuitionOpt.get());
                logger.info("✅ 成功获取学费记录: ID={}", id);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("未找到学费记录: ID={}", id);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("获取学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "获取所有学费记录", description = "获取系统中所有学费记录")
    public ResponseEntity<List<TuitionResponse>> getAllTuitions() {
        logger.info("=== TuitionController.getAllTuitions() 被调用 ===");

        try {
            List<Tuition> tuitions = tuitionService.getAllTuitions();
            
            List<TuitionResponse> responses = tuitions.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取 {} 条学费记录", responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "根据学生ID获取学费记录", description = "根据学生ID获取学费记录列表")
    public ResponseEntity<List<TuitionResponse>> getTuitionsByStudent(@PathVariable Long studentId) {
        logger.info("=== TuitionController.getTuitionsByStudent() 被调用 ===");
        logger.info("查询学生学费记录: 学生ID={}", studentId);

        try {
            List<Tuition> tuitions = tuitionService.getTuitionsByStudent(studentId);
            
            List<TuitionResponse> responses = tuitions.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学生 {} 的 {} 条学费记录", studentId, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/number/{studentNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "根据学号获取学费记录", description = "根据学号获取学费记录列表")
    public ResponseEntity<List<TuitionResponse>> getTuitionsByStudentNumber(@PathVariable String studentNumber) {
        logger.info("=== TuitionController.getTuitionsByStudentNumber() 被调用 ===");
        logger.info("查询学生学费记录: 学号={}", studentNumber);

        try {
            List<Tuition> tuitions = tuitionService.getTuitionsByStudentStudentId(studentNumber);
            
            List<TuitionResponse> responses = tuitions.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学号 {} 的 {} 条学费记录", studentNumber, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/number/{studentNumber}/semester/{semester}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "根据学号和学期获取学费记录", description = "根据学号和学期获取学费记录")
    public ResponseEntity<TuitionResponse> getTuitionByStudentNumberAndSemester(
            @PathVariable String studentNumber, @PathVariable String semester) {
        logger.info("=== TuitionController.getTuitionByStudentNumberAndSemester() 被调用 ===");
        logger.info("查询学生学期学费记录: 学号={}, 学期={}", studentNumber, semester);

        try {
            Optional<Tuition> tuitionOpt = tuitionService.getTuitionByStudentStudentIdAndSemester(studentNumber, semester);
            
            if (tuitionOpt.isPresent()) {
                TuitionResponse response = convertToResponse(tuitionOpt.get());
                logger.info("✅ 成功获取学号 {} 学期 {} 的学费记录", studentNumber, semester);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("未找到学号 {} 学期 {} 的学费记录", studentNumber, semester);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("获取学生学期学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/semester/{semester}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "根据学期获取学费记录", description = "根据学期获取学费记录列表")
    public ResponseEntity<List<TuitionResponse>> getTuitionsBySemester(@PathVariable String semester) {
        logger.info("=== TuitionController.getTuitionsBySemester() 被调用 ===");
        logger.info("查询学期学费记录: 学期={}", semester);

        try {
            List<Tuition> tuitions = tuitionService.getTuitionsBySemester(semester);
            
            List<TuitionResponse> responses = tuitions.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学期 {} 的 {} 条学费记录", semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学期学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "根据状态获取学费记录", description = "根据状态获取学费记录列表")
    public ResponseEntity<List<TuitionResponse>> getTuitionsByStatus(@PathVariable Integer status) {
        logger.info("=== TuitionController.getTuitionsByStatus() 被调用 ===");
        logger.info("查询状态学费记录: 状态={}", status);

        try {
            List<Tuition> tuitions = tuitionService.getTuitionsByStatus(status);
            
            List<TuitionResponse> responses = tuitions.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取状态 {} 的 {} 条学费记录", status, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取状态学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "获取逾期未缴学费记录", description = "获取逾期未缴的学费记录列表")
    public ResponseEntity<List<TuitionResponse>> getOverdueTuitions() {
        logger.info("=== TuitionController.getOverdueTuitions() 被调用 ===");

        try {
            List<Tuition> tuitions = tuitionService.getOverdueTuitions();
            
            List<TuitionResponse> responses = tuitions.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取 {} 条逾期未缴学费记录", responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取逾期未缴学费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/payment")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(summary = "学费缴费", description = "为学生缴纳学费")
    public ResponseEntity<TuitionResponse> makePayment(@RequestBody TuitionPaymentRequest request) {
        logger.info("=== TuitionController.makePayment() 被调用 ===");
        logger.info("学费缴费: 学费ID={}, 缴费金额={}", request.getTuitionId(), request.getPaymentAmount());

        try {
            Tuition tuition = tuitionService.makePayment(request.getTuitionId(), request.getPaymentAmount());
            
            // 转换为响应DTO
            TuitionResponse response = convertToResponse(tuition);
            
            logger.info("✅ 成功缴费: 学费ID={}, 缴费金额={}", tuition.getId(), request.getPaymentAmount());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("学费缴费失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/statistics/{semester}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "获取学费统计信息", description = "获取指定学期的学费统计信息")
    public ResponseEntity<TuitionService.TuitionStatistics> getTuitionStatistics(@PathVariable String semester) {
        logger.info("=== TuitionController.getTuitionStatistics() 被调用 ===");
        logger.info("查询学费统计: 学期={}", semester);

        try {
            TuitionService.TuitionStatistics statistics = tuitionService.getTuitionStatistics(semester);
            
            logger.info("✅ 成功获取学期 {} 的学费统计信息", semester);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("获取学费统计信息失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics/department/{department}/{semester}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "获取院系学费统计信息", description = "获取指定院系和学期的学费统计信息")
    public ResponseEntity<TuitionService.DepartmentTuitionStatistics> getDepartmentTuitionStatistics(
            @PathVariable String department, @PathVariable String semester) {
        logger.info("=== TuitionController.getDepartmentTuitionStatistics() 被调用 ===");
        logger.info("查询院系学费统计: 院系={}, 学期={}", department, semester);

        try {
            TuitionService.DepartmentTuitionStatistics statistics = 
                    tuitionService.getDepartmentTuitionStatistics(department, semester);
            
            logger.info("✅ 成功获取院系 {} 学期 {} 的学费统计信息", department, semester);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("获取院系学费统计信息失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 将Tuition实体转换为TuitionResponse DTO
     */
    private TuitionResponse convertToResponse(Tuition tuition) {
        TuitionResponse response = new TuitionResponse();
        response.setId(tuition.getId());
        response.setStudentId(tuition.getStudent().getId());
        response.setStudentName(tuition.getStudent().getRealName());
        response.setStudentNumber(tuition.getStudent().getStudentId());
        response.setAmount(tuition.getAmount());
        response.setPaidAmount(tuition.getPaidAmount());
        response.setUnpaidAmount(tuition.getUnpaidAmount());
        response.setSemester(tuition.getSemester());
        response.setDueDate(tuition.getDueDate());
        response.setStatus(tuition.getStatus());
        response.setStatusDescription(getStatusDescription(tuition.getStatus()));
        response.setDescription(tuition.getDescription());
        response.setCreateTime(tuition.getCreateTime());
        response.setUpdateTime(tuition.getUpdateTime());
        return response;
    }

    /**
     * 获取状态描述
     */
    private String getStatusDescription(Integer status) {
        switch (status) {
            case 0:
                return "未缴费";
            case 1:
                return "部分缴费";
            case 2:
                return "已缴费";
            default:
                return "未知状态";
        }
    }
}