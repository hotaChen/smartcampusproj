package com.example.smartcampus.controller;

import com.example.smartcampus.dto.PaymentRecordRequest;
import com.example.smartcampus.dto.PaymentRecordResponse;
import com.example.smartcampus.entity.PaymentRecord;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.service.PaymentRecordService;
import com.example.smartcampus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment-records")
@Tag(name = "缴费记录管理", description = "缴费记录信息管理相关接口")
public class PaymentRecordController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRecordController.class);

    private final PaymentRecordService paymentRecordService;
    private final UserService userService;

    public PaymentRecordController(PaymentRecordService paymentRecordService, UserService userService) {
        this.paymentRecordService = paymentRecordService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "创建缴费记录", description = "为学生创建缴费记录")
    public ResponseEntity<PaymentRecordResponse> createPaymentRecord(@RequestBody PaymentRecordRequest request) {
        logger.info("=== PaymentRecordController.createPaymentRecord() 被调用 ===");
        logger.info("创建缴费记录: 学生ID={}, 缴费类型={}, 金额={}", 
                request.getStudentId(), request.getPaymentType(), request.getAmount());

        try {
            // 获取学生信息
            User student = userService.getUserById(request.getStudentId());
            
            // 创建缴费记录
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setStudent(student);
            paymentRecord.setAmount(request.getAmount());
            paymentRecord.setPaymentType(request.getPaymentType());
            paymentRecord.setPaymentMethod(request.getPaymentMethod());
            paymentRecord.setPaymentDate(request.getPaymentDate());
            paymentRecord.setSemester(request.getSemester());
            paymentRecord.setTransactionId(request.getTransactionId());
            paymentRecord.setDescription(request.getDescription());
            paymentRecord.setStatus(request.getStatus());
            
            PaymentRecord savedPaymentRecord = paymentRecordService.createPaymentRecord(paymentRecord);
            
            // 转换为响应DTO
            PaymentRecordResponse response = convertToResponse(savedPaymentRecord);
            
            logger.info("✅ 成功创建缴费记录: ID={}", savedPaymentRecord.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("创建缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新缴费记录", description = "更新缴费记录信息")
    public ResponseEntity<PaymentRecordResponse> updatePaymentRecord(@PathVariable Long id, @RequestBody PaymentRecordRequest request) {
        logger.info("=== PaymentRecordController.updatePaymentRecord() 被调用 ===");
        logger.info("更新缴费记录: ID={}", id);

        try {
            // 获取现有缴费记录
            Optional<PaymentRecord> paymentRecordOpt = paymentRecordService.getPaymentRecordById(id);
            if (!paymentRecordOpt.isPresent()) {
                logger.warn("未找到缴费记录: ID={}", id);
                return ResponseEntity.notFound().build();
            }
            
            PaymentRecord existingPaymentRecord = paymentRecordOpt.get();
            
            // 更新字段
            if (request.getAmount() != null) {
                existingPaymentRecord.setAmount(request.getAmount());
            }
            if (request.getPaymentType() != null) {
                existingPaymentRecord.setPaymentType(request.getPaymentType());
            }
            if (request.getPaymentMethod() != null) {
                existingPaymentRecord.setPaymentMethod(request.getPaymentMethod());
            }
            if (request.getPaymentDate() != null) {
                existingPaymentRecord.setPaymentDate(request.getPaymentDate());
            }
            if (request.getSemester() != null) {
                existingPaymentRecord.setSemester(request.getSemester());
            }
            if (request.getTransactionId() != null) {
                existingPaymentRecord.setTransactionId(request.getTransactionId());
            }
            if (request.getDescription() != null) {
                existingPaymentRecord.setDescription(request.getDescription());
            }
            if (request.getStatus() != null) {
                existingPaymentRecord.setStatus(request.getStatus());
            }
            
            PaymentRecord updatedPaymentRecord = paymentRecordService.updatePaymentRecord(id, existingPaymentRecord);
            
            // 转换为响应DTO
            PaymentRecordResponse response = convertToResponse(updatedPaymentRecord);
            
            logger.info("✅ 成功更新缴费记录: ID={}", updatedPaymentRecord.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("更新缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除缴费记录", description = "删除缴费记录")
    public ResponseEntity<Void> deletePaymentRecord(@PathVariable Long id) {
        logger.info("=== PaymentRecordController.deletePaymentRecord() 被调用 ===");
        logger.info("删除缴费记录: ID={}", id);

        try {
            paymentRecordService.deletePaymentRecord(id);
            logger.info("✅ 成功删除缴费记录: ID={}", id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("删除缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取缴费记录", description = "根据ID获取缴费记录详情")
    public ResponseEntity<PaymentRecordResponse> getPaymentRecordById(@PathVariable Long id) {
        logger.info("=== PaymentRecordController.getPaymentRecordById() 被调用 ===");
        logger.info("查询缴费记录: ID={}", id);

        try {
            Optional<PaymentRecord> paymentRecordOpt = paymentRecordService.getPaymentRecordById(id);
            
            if (paymentRecordOpt.isPresent()) {
                PaymentRecordResponse response = convertToResponse(paymentRecordOpt.get());
                logger.info("✅ 成功获取缴费记录: ID={}", id);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("未找到缴费记录: ID={}", id);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("获取缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @Operation(summary = "获取所有缴费记录", description = "获取系统中所有缴费记录")
    public ResponseEntity<List<PaymentRecordResponse>> getAllPaymentRecords() {
        logger.info("=== PaymentRecordController.getAllPaymentRecords() 被调用 ===");

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getAllPaymentRecords();
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取 {} 条缴费记录", responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "根据学生ID获取缴费记录", description = "根据学生ID获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsByStudent(@PathVariable Long studentId) {
        logger.info("=== PaymentRecordController.getPaymentRecordsByStudent() 被调用 ===");
        logger.info("查询学生缴费记录: 学生ID={}", studentId);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsByStudent(studentId);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学生 {} 的 {} 条缴费记录", studentId, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/number/{studentNumber}")
    @Operation(summary = "根据学号获取缴费记录", description = "根据学号获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsByStudentNumber(@PathVariable String studentNumber) {
        logger.info("=== PaymentRecordController.getPaymentRecordsByStudentNumber() 被调用 ===");
        logger.info("查询学生缴费记录: 学号={}", studentNumber);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsByStudentStudentId(studentNumber);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学号 {} 的 {} 条缴费记录", studentNumber, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/semester/{semester}")
    @Operation(summary = "根据学期获取缴费记录", description = "根据学期获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsBySemester(@PathVariable String semester) {
        logger.info("=== PaymentRecordController.getPaymentRecordsBySemester() 被调用 ===");
        logger.info("查询学期缴费记录: 学期={}", semester);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsBySemester(semester);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学期 {} 的 {} 条缴费记录", semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学期缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取缴费记录", description = "根据状态获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsByStatus(@PathVariable Integer status) {
        logger.info("=== PaymentRecordController.getPaymentRecordsByStatus() 被调用 ===");
        logger.info("查询状态缴费记录: 状态={}", status);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsByStatus(status);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取状态 {} 的 {} 条缴费记录", status, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取状态缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/payment-type/{paymentType}")
    @Operation(summary = "根据缴费类型获取缴费记录", description = "根据缴费类型获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsByPaymentType(@PathVariable String paymentType) {
        logger.info("=== PaymentRecordController.getPaymentRecordsByPaymentType() 被调用 ===");
        logger.info("查询缴费类型记录: 缴费类型={}", paymentType);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsByPaymentType(paymentType);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取缴费类型 {} 的 {} 条记录", paymentType, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取缴费类型记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/{studentId}/semester/{semester}")
    @Operation(summary = "根据学生ID和学期获取缴费记录", description = "根据学生ID和学期获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsByStudentAndSemester(
            @PathVariable Long studentId, @PathVariable String semester) {
        logger.info("=== PaymentRecordController.getPaymentRecordsByStudentAndSemester() 被调用 ===");
        logger.info("查询学生学期缴费记录: 学生ID={}, 学期={}", studentId, semester);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsByStudentAndSemester(studentId, semester);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学生 {} 学期 {} 的 {} 条缴费记录", studentId, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生学期缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/number/{studentNumber}/semester/{semester}")
    @Operation(summary = "根据学号和学期获取缴费记录", description = "根据学号和学期获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsByStudentNumberAndSemester(
            @PathVariable String studentNumber, @PathVariable String semester) {
        logger.info("=== PaymentRecordController.getPaymentRecordsByStudentNumberAndSemester() 被调用 ===");
        logger.info("查询学生学期缴费记录: 学号={}, 学期={}", studentNumber, semester);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsByStudentStudentIdAndSemester(studentNumber, semester);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学号 {} 学期 {} 的 {} 条缴费记录", studentNumber, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生学期缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/department/{department}/semester/{semester}")
    @Operation(summary = "根据院系和学期获取缴费记录", description = "根据院系和学期获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsByDepartmentAndSemester(
            @PathVariable String department, @PathVariable String semester) {
        logger.info("=== PaymentRecordController.getPaymentRecordsByDepartmentAndSemester() 被调用 ===");
        logger.info("查询院系学期缴费记录: 院系={}, 学期={}", department, semester);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsByDepartmentAndSemester(department, semester);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取院系 {} 学期 {} 的 {} 条缴费记录", department, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取院系学期缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/major/{major}/semester/{semester}")
    @Operation(summary = "根据专业和学期获取缴费记录", description = "根据专业和学期获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsByMajorAndSemester(
            @PathVariable String major, @PathVariable String semester) {
        logger.info("=== PaymentRecordController.getPaymentRecordsByMajorAndSemester() 被调用 ===");
        logger.info("查询专业学期缴费记录: 专业={}, 学期={}", major, semester);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsByMajorAndSemester(major, semester);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取专业 {} 学期 {} 的 {} 条缴费记录", major, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取专业学期缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/grade/{grade}/semester/{semester}")
    @Operation(summary = "根据年级和学期获取缴费记录", description = "根据年级和学期获取缴费记录列表")
    public ResponseEntity<List<PaymentRecordResponse>> getPaymentRecordsByGradeAndSemester(
            @PathVariable Integer grade, @PathVariable String semester) {
        logger.info("=== PaymentRecordController.getPaymentRecordsByGradeAndSemester() 被调用 ===");
        logger.info("查询年级学期缴费记录: 年级={}, 学期={}", grade, semester);

        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecordsByGradeAndSemester(grade, semester);
            
            List<PaymentRecordResponse> responses = paymentRecords.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取年级 {} 学期 {} 的 {} 条缴费记录", grade, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取年级学期缴费记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics/{semester}")
    @Operation(summary = "获取缴费统计信息", description = "获取指定学期的缴费统计信息")
    public ResponseEntity<PaymentRecordService.PaymentRecordStatistics> getPaymentRecordStatistics(@PathVariable String semester) {
        logger.info("=== PaymentRecordController.getPaymentRecordStatistics() 被调用 ===");
        logger.info("查询缴费统计: 学期={}", semester);

        try {
            PaymentRecordService.PaymentRecordStatistics statistics = paymentRecordService.getPaymentRecordStatistics(semester);
            
            logger.info("✅ 成功获取学期 {} 的缴费统计信息", semester);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("获取缴费统计信息失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics/department/{department}/{semester}")
    @Operation(summary = "获取院系缴费统计信息", description = "获取指定院系和学期的缴费统计信息")
    public ResponseEntity<PaymentRecordService.DepartmentPaymentRecordStatistics> getDepartmentPaymentRecordStatistics(
            @PathVariable String department, @PathVariable String semester) {
        logger.info("=== PaymentRecordController.getDepartmentPaymentRecordStatistics() 被调用 ===");
        logger.info("查询院系缴费统计: 院系={}, 学期={}", department, semester);

        try {
            PaymentRecordService.DepartmentPaymentRecordStatistics statistics = 
                    paymentRecordService.getDepartmentPaymentRecordStatistics(department, semester);
            
            logger.info("✅ 成功获取院系 {} 学期 {} 的缴费统计信息", department, semester);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("获取院系缴费统计信息失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 将PaymentRecord实体转换为PaymentRecordResponse DTO
     */
    private PaymentRecordResponse convertToResponse(PaymentRecord paymentRecord) {
        PaymentRecordResponse response = new PaymentRecordResponse();
        response.setId(paymentRecord.getId());
        response.setStudentId(paymentRecord.getStudent().getId());
        response.setStudentName(paymentRecord.getStudent().getRealName());
        response.setStudentNumber(paymentRecord.getStudent().getStudentId());
        response.setAmount(paymentRecord.getAmount());
        response.setPaymentType(paymentRecord.getPaymentType());
        response.setPaymentMethod(paymentRecord.getPaymentMethod());
        response.setPaymentDate(paymentRecord.getPaymentDate());
        response.setSemester(paymentRecord.getSemester());
        response.setTransactionId(paymentRecord.getTransactionId());
        response.setDescription(paymentRecord.getDescription());
        response.setStatus(paymentRecord.getStatus());
        response.setCreateTime(paymentRecord.getCreateTime());
        
        // 添加学生详细信息
        if (paymentRecord.getStudent().getDepartment() != null) {
            response.setDepartment(paymentRecord.getStudent().getDepartment());
        }
        if (paymentRecord.getStudent().getMajor() != null) {
            response.setMajor(paymentRecord.getStudent().getMajor());
        }
        if (paymentRecord.getStudent().getGrade() != null) {
            response.setGrade(paymentRecord.getStudent().getGrade());
        }
        if (paymentRecord.getStudent().getClassName() != null) {
            response.setClassName(paymentRecord.getStudent().getClassName());
        }
        
        return response;
    }
}