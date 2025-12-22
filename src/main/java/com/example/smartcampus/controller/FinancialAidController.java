package com.example.smartcampus.controller;

import com.example.smartcampus.dto.FinancialAidRequest;
import com.example.smartcampus.dto.FinancialAidResponse;
import com.example.smartcampus.entity.FinancialAid;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.service.FinancialAidService;
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
@RequestMapping("/api/financial-aids")
@Tag(name = "助学金管理", description = "助学金信息管理相关接口")
public class FinancialAidController {

    private static final Logger logger = LoggerFactory.getLogger(FinancialAidController.class);

    private final FinancialAidService financialAidService;
    private final UserService userService;

    public FinancialAidController(FinancialAidService financialAidService, UserService userService) {
        this.financialAidService = financialAidService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "创建助学金记录", description = "为学生创建助学金记录")
    public ResponseEntity<FinancialAidResponse> createFinancialAid(@RequestBody FinancialAidRequest request) {
        logger.info("=== FinancialAidController.createFinancialAid() 被调用 ===");
        logger.info("创建助学金记录: 学生ID={}, 助学金类型={}, 金额={}", 
                request.getStudentId(), request.getType(), request.getAmount());

        try {
            // 获取学生信息
            User student = userService.getUserById(request.getStudentId());
            
            // 创建助学金记录
            FinancialAid financialAid = new FinancialAid();
            financialAid.setStudent(student);
            financialAid.setAmount(request.getAmount());
            financialAid.setName(request.getName());
            financialAid.setType(request.getType());
            financialAid.setSemester(request.getSemester());
            financialAid.setAwardDate(request.getAwardDate());
            financialAid.setDescription(request.getDescription());
            financialAid.setReason(request.getReason());
            financialAid.setStatus(request.getStatus());
            
            FinancialAid savedFinancialAid = financialAidService.createFinancialAid(financialAid);
            
            // 转换为响应DTO
            FinancialAidResponse response = convertToResponse(savedFinancialAid);
            
            logger.info("✅ 成功创建助学金记录: ID={}", savedFinancialAid.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("创建助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新助学金记录", description = "更新助学金记录信息")
    public ResponseEntity<FinancialAidResponse> updateFinancialAid(@PathVariable Long id, @RequestBody FinancialAidRequest request) {
        logger.info("=== FinancialAidController.updateFinancialAid() 被调用 ===");
        logger.info("更新助学金记录: ID={}", id);

        try {
            // 获取现有助学金记录
            Optional<FinancialAid> financialAidOpt = financialAidService.getFinancialAidById(id);
            if (!financialAidOpt.isPresent()) {
                logger.warn("未找到助学金记录: ID={}", id);
                return ResponseEntity.notFound().build();
            }
            
            FinancialAid existingFinancialAid = financialAidOpt.get();
            
            // 更新字段
            if (request.getAmount() != null) {
                existingFinancialAid.setAmount(request.getAmount());
            }
            if (request.getName() != null) {
                existingFinancialAid.setName(request.getName());
            }
            if (request.getType() != null) {
                existingFinancialAid.setType(request.getType());
            }
            if (request.getSemester() != null) {
                existingFinancialAid.setSemester(request.getSemester());
            }
            if (request.getAwardDate() != null) {
                existingFinancialAid.setAwardDate(request.getAwardDate());
            }
            if (request.getDescription() != null) {
                existingFinancialAid.setDescription(request.getDescription());
            }
            if (request.getReason() != null) {
                existingFinancialAid.setReason(request.getReason());
            }
            if (request.getStatus() != null) {
                existingFinancialAid.setStatus(request.getStatus());
            }
            
            FinancialAid updatedFinancialAid = financialAidService.updateFinancialAid(id, existingFinancialAid);
            
            // 转换为响应DTO
            FinancialAidResponse response = convertToResponse(updatedFinancialAid);
            
            logger.info("✅ 成功更新助学金记录: ID={}", updatedFinancialAid.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("更新助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除助学金记录", description = "删除助学金记录")
    public ResponseEntity<Void> deleteFinancialAid(@PathVariable Long id) {
        logger.info("=== FinancialAidController.deleteFinancialAid() 被调用 ===");
        logger.info("删除助学金记录: ID={}", id);

        try {
            financialAidService.deleteFinancialAid(id);
            logger.info("✅ 成功删除助学金记录: ID={}", id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("删除助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取助学金记录", description = "根据ID获取助学金记录详情")
    public ResponseEntity<FinancialAidResponse> getFinancialAidById(@PathVariable Long id) {
        logger.info("=== FinancialAidController.getFinancialAidById() 被调用 ===");
        logger.info("查询助学金记录: ID={}", id);

        try {
            Optional<FinancialAid> financialAidOpt = financialAidService.getFinancialAidById(id);
            
            if (financialAidOpt.isPresent()) {
                FinancialAidResponse response = convertToResponse(financialAidOpt.get());
                logger.info("✅ 成功获取助学金记录: ID={}", id);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("未找到助学金记录: ID={}", id);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("获取助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @Operation(summary = "获取所有助学金记录", description = "获取系统中所有助学金记录")
    public ResponseEntity<List<FinancialAidResponse>> getAllFinancialAids() {
        logger.info("=== FinancialAidController.getAllFinancialAids() 被调用 ===");

        try {
            List<FinancialAid> financialAids = financialAidService.getAllFinancialAids();
            
            List<FinancialAidResponse> responses = financialAids.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取 {} 条助学金记录", responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "根据学生ID获取助学金记录", description = "根据学生ID获取助学金记录列表")
    public ResponseEntity<List<FinancialAidResponse>> getFinancialAidsByStudent(@PathVariable Long studentId) {
        logger.info("=== FinancialAidController.getFinancialAidsByStudent() 被调用 ===");
        logger.info("查询学生助学金记录: 学生ID={}", studentId);

        try {
            List<FinancialAid> financialAids = financialAidService.getFinancialAidsByStudent(studentId);
            
            List<FinancialAidResponse> responses = financialAids.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学生 {} 的 {} 条助学金记录", studentId, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/semester/{semester}")
    @Operation(summary = "根据学期获取助学金记录", description = "根据学期获取助学金记录列表")
    public ResponseEntity<List<FinancialAidResponse>> getFinancialAidsBySemester(@PathVariable String semester) {
        logger.info("=== FinancialAidController.getFinancialAidsBySemester() 被调用 ===");
        logger.info("查询学期助学金记录: 学期={}", semester);

        try {
            List<FinancialAid> financialAids = financialAidService.getFinancialAidsBySemester(semester);
            
            List<FinancialAidResponse> responses = financialAids.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学期 {} 的 {} 条助学金记录", semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学期助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取助学金记录", description = "根据状态获取助学金记录列表")
    public ResponseEntity<List<FinancialAidResponse>> getFinancialAidsByStatus(@PathVariable Integer status) {
        logger.info("=== FinancialAidController.getFinancialAidsByStatus() 被调用 ===");
        logger.info("查询状态助学金记录: 状态={}", status);

        try {
            List<FinancialAid> financialAids = financialAidService.getFinancialAidsByStatus(status);
            
            List<FinancialAidResponse> responses = financialAids.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取状态 {} 的 {} 条助学金记录", status, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取状态助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型获取助学金记录", description = "根据类型获取助学金记录列表")
    public ResponseEntity<List<FinancialAidResponse>> getFinancialAidsByType(@PathVariable String type) {
        logger.info("=== FinancialAidController.getFinancialAidsByType() 被调用 ===");
        logger.info("查询类型助学金记录: 类型={}", type);

        try {
            List<FinancialAid> financialAids = financialAidService.getFinancialAidsByType(type);
            
            List<FinancialAidResponse> responses = financialAids.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取类型 {} 的 {} 条助学金记录", type, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取类型助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/{studentId}/semester/{semester}")
    @Operation(summary = "根据学生ID和学期获取助学金记录", description = "根据学生ID和学期获取助学金记录列表")
    public ResponseEntity<List<FinancialAidResponse>> getFinancialAidsByStudentAndSemester(
            @PathVariable Long studentId, @PathVariable String semester) {
        logger.info("=== FinancialAidController.getFinancialAidsByStudentAndSemester() 被调用 ===");
        logger.info("查询学生学期助学金记录: 学生ID={}, 学期={}", studentId, semester);

        try {
            List<FinancialAid> financialAids = financialAidService.getFinancialAidsByStudentAndSemester(studentId, semester);
            
            List<FinancialAidResponse> responses = financialAids.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学生 {} 学期 {} 的 {} 条助学金记录", studentId, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生学期助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/department/{department}/semester/{semester}")
    @Operation(summary = "根据院系和学期获取助学金记录", description = "根据院系和学期获取助学金记录列表")
    public ResponseEntity<List<FinancialAidResponse>> getFinancialAidsByDepartmentAndSemester(
            @PathVariable String department, @PathVariable String semester) {
        logger.info("=== FinancialAidController.getFinancialAidsByDepartmentAndSemester() 被调用 ===");
        logger.info("查询院系学期助学金记录: 院系={}, 学期={}", department, semester);

        try {
            List<FinancialAid> financialAids = financialAidService.getFinancialAidsByDepartmentAndSemester(department, semester);
            
            List<FinancialAidResponse> responses = financialAids.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取院系 {} 学期 {} 的 {} 条助学金记录", department, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取院系学期助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/major/{major}/semester/{semester}")
    @Operation(summary = "根据专业和学期获取助学金记录", description = "根据专业和学期获取助学金记录列表")
    public ResponseEntity<List<FinancialAidResponse>> getFinancialAidsByMajorAndSemester(
            @PathVariable String major, @PathVariable String semester) {
        logger.info("=== FinancialAidController.getFinancialAidsByMajorAndSemester() 被调用 ===");
        logger.info("查询专业学期助学金记录: 专业={}, 学期={}", major, semester);

        try {
            List<FinancialAid> financialAids = financialAidService.getFinancialAidsByMajorAndSemester(major, semester);
            
            List<FinancialAidResponse> responses = financialAids.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取专业 {} 学期 {} 的 {} 条助学金记录", major, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取专业学期助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/grade/{grade}/semester/{semester}")
    @Operation(summary = "根据年级和学期获取助学金记录", description = "根据年级和学期获取助学金记录列表")
    public ResponseEntity<List<FinancialAidResponse>> getFinancialAidsByGradeAndSemester(
            @PathVariable Integer grade, @PathVariable String semester) {
        logger.info("=== FinancialAidController.getFinancialAidsByGradeAndSemester() 被调用 ===");
        logger.info("查询年级学期助学金记录: 年级={}, 学期={}", grade, semester);

        try {
            List<FinancialAid> financialAids = financialAidService.getFinancialAidsByGradeAndSemester(grade, semester);
            
            List<FinancialAidResponse> responses = financialAids.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取年级 {} 学期 {} 的 {} 条助学金记录", grade, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取年级学期助学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics/{semester}")
    @Operation(summary = "获取助学金统计信息", description = "获取指定学期的助学金统计信息")
    public ResponseEntity<FinancialAidService.FinancialAidStatistics> getFinancialAidStatistics(@PathVariable String semester) {
        logger.info("=== FinancialAidController.getFinancialAidStatistics() 被调用 ===");
        logger.info("查询助学金统计: 学期={}", semester);

        try {
            FinancialAidService.FinancialAidStatistics statistics = financialAidService.getFinancialAidStatistics(semester);
            
            logger.info("✅ 成功获取学期 {} 的助学金统计信息", semester);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("获取助学金统计信息失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics/department/{department}/{semester}")
    @Operation(summary = "获取院系助学金统计信息", description = "获取指定院系和学期的助学金统计信息")
    public ResponseEntity<FinancialAidService.DepartmentFinancialAidStatistics> getDepartmentFinancialAidStatistics(
            @PathVariable String department, @PathVariable String semester) {
        logger.info("=== FinancialAidController.getDepartmentFinancialAidStatistics() 被调用 ===");
        logger.info("查询院系助学金统计: 院系={}, 学期={}", department, semester);

        try {
            FinancialAidService.DepartmentFinancialAidStatistics statistics = 
                    financialAidService.getDepartmentFinancialAidStatistics(department, semester);
            
            logger.info("✅ 成功获取院系 {} 学期 {} 的助学金统计信息", department, semester);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("获取院系助学金统计信息失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 将FinancialAid实体转换为FinancialAidResponse DTO
     */
    private FinancialAidResponse convertToResponse(FinancialAid financialAid) {
        FinancialAidResponse response = new FinancialAidResponse();
        response.setId(financialAid.getId());
        response.setStudentId(financialAid.getStudent().getId());
        response.setStudentName(financialAid.getStudent().getRealName());
        response.setStudentNumber(financialAid.getStudent().getStudentId());
        response.setAmount(financialAid.getAmount());
        response.setName(financialAid.getName());
        response.setType(financialAid.getType());
        response.setSemester(financialAid.getSemester());
        response.setAwardDate(financialAid.getAwardDate());
        response.setDescription(financialAid.getDescription());
        response.setReason(financialAid.getReason());
        response.setStatus(financialAid.getStatus());
        response.setCreateTime(financialAid.getCreateTime());
        response.setUpdateTime(financialAid.getUpdateTime());
        
        // 添加学生详细信息
        if (financialAid.getStudent().getDepartment() != null) {
            response.setDepartment(financialAid.getStudent().getDepartment());
        }
        if (financialAid.getStudent().getMajor() != null) {
            response.setMajor(financialAid.getStudent().getMajor());
        }
        if (financialAid.getStudent().getGrade() != null) {
            response.setGrade(financialAid.getStudent().getGrade());
        }
        if (financialAid.getStudent().getClassName() != null) {
            response.setClassName(financialAid.getStudent().getClassName());
        }
        
        return response;
    }
}