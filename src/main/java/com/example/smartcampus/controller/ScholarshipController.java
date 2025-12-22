package com.example.smartcampus.controller;

import com.example.smartcampus.dto.ScholarshipRequest;
import com.example.smartcampus.dto.ScholarshipResponse;
import com.example.smartcampus.entity.Scholarship;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.service.ScholarshipService;
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
@RequestMapping("/api/scholarships")
@Tag(name = "奖学金管理", description = "奖学金信息管理相关接口")
public class ScholarshipController {

    private static final Logger logger = LoggerFactory.getLogger(ScholarshipController.class);

    private final ScholarshipService scholarshipService;
    private final UserService userService;

    public ScholarshipController(ScholarshipService scholarshipService, UserService userService) {
        this.scholarshipService = scholarshipService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "创建奖学金记录", description = "为学生创建奖学金记录")
    public ResponseEntity<ScholarshipResponse> createScholarship(@RequestBody ScholarshipRequest request) {
        logger.info("=== ScholarshipController.createScholarship() 被调用 ===");
        logger.info("创建奖学金记录: 学生ID={}, 奖学金类型={}, 金额={}", 
                request.getStudentId(), request.getType(), request.getAmount());

        try {
            // 获取学生信息
            User student = userService.getUserById(request.getStudentId());
            
            // 创建奖学金记录
            Scholarship scholarship = new Scholarship();
            scholarship.setStudent(student);
            scholarship.setAmount(request.getAmount());
            scholarship.setName(request.getName());
            scholarship.setType(request.getType());
            scholarship.setSemester(request.getSemester());
            scholarship.setAwardDate(request.getAwardDate());
            scholarship.setDescription(request.getDescription());
            scholarship.setReason(request.getReason());
            scholarship.setStatus(request.getStatus());
            
            Scholarship savedScholarship = scholarshipService.createScholarship(scholarship);
            
            // 转换为响应DTO
            ScholarshipResponse response = convertToResponse(savedScholarship);
            
            logger.info("✅ 成功创建奖学金记录: ID={}", savedScholarship.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("创建奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新奖学金记录", description = "更新奖学金记录信息")
    public ResponseEntity<ScholarshipResponse> updateScholarship(@PathVariable Long id, @RequestBody ScholarshipRequest request) {
        logger.info("=== ScholarshipController.updateScholarship() 被调用 ===");
        logger.info("更新奖学金记录: ID={}", id);

        try {
            // 获取现有奖学金记录
            Optional<Scholarship> scholarshipOpt = scholarshipService.getScholarshipById(id);
            if (!scholarshipOpt.isPresent()) {
                logger.warn("未找到奖学金记录: ID={}", id);
                return ResponseEntity.notFound().build();
            }
            
            Scholarship existingScholarship = scholarshipOpt.get();
            
            // 更新字段
            if (request.getAmount() != null) {
                existingScholarship.setAmount(request.getAmount());
            }
            if (request.getName() != null) {
                existingScholarship.setName(request.getName());
            }
            if (request.getType() != null) {
                existingScholarship.setType(request.getType());
            }
            if (request.getSemester() != null) {
                existingScholarship.setSemester(request.getSemester());
            }
            if (request.getAwardDate() != null) {
                existingScholarship.setAwardDate(request.getAwardDate());
            }
            if (request.getDescription() != null) {
                existingScholarship.setDescription(request.getDescription());
            }
            if (request.getReason() != null) {
                existingScholarship.setReason(request.getReason());
            }
            if (request.getStatus() != null) {
                existingScholarship.setStatus(request.getStatus());
            }
            
            Scholarship updatedScholarship = scholarshipService.updateScholarship(id, existingScholarship);
            
            // 转换为响应DTO
            ScholarshipResponse response = convertToResponse(updatedScholarship);
            
            logger.info("✅ 成功更新奖学金记录: ID={}", updatedScholarship.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("更新奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除奖学金记录", description = "删除奖学金记录")
    public ResponseEntity<Void> deleteScholarship(@PathVariable Long id) {
        logger.info("=== ScholarshipController.deleteScholarship() 被调用 ===");
        logger.info("删除奖学金记录: ID={}", id);

        try {
            scholarshipService.deleteScholarship(id);
            logger.info("✅ 成功删除奖学金记录: ID={}", id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("删除奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取奖学金记录", description = "根据ID获取奖学金记录详情")
    public ResponseEntity<ScholarshipResponse> getScholarshipById(@PathVariable Long id) {
        logger.info("=== ScholarshipController.getScholarshipById() 被调用 ===");
        logger.info("查询奖学金记录: ID={}", id);

        try {
            Optional<Scholarship> scholarshipOpt = scholarshipService.getScholarshipById(id);
            
            if (scholarshipOpt.isPresent()) {
                ScholarshipResponse response = convertToResponse(scholarshipOpt.get());
                logger.info("✅ 成功获取奖学金记录: ID={}", id);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("未找到奖学金记录: ID={}", id);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("获取奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @Operation(summary = "获取所有奖学金记录", description = "获取系统中所有奖学金记录")
    public ResponseEntity<List<ScholarshipResponse>> getAllScholarships() {
        logger.info("=== ScholarshipController.getAllScholarships() 被调用 ===");

        try {
            List<Scholarship> scholarships = scholarshipService.getAllScholarships();
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取 {} 条奖学金记录", responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "根据学生ID获取奖学金记录", description = "根据学生ID获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsByStudent(@PathVariable Long studentId) {
        logger.info("=== ScholarshipController.getScholarshipsByStudent() 被调用 ===");
        logger.info("查询学生奖学金记录: 学生ID={}", studentId);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsByStudent(studentId);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学生 {} 的 {} 条奖学金记录", studentId, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/number/{studentNumber}")
    @Operation(summary = "根据学号获取奖学金记录", description = "根据学号获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsByStudentNumber(@PathVariable String studentNumber) {
        logger.info("=== ScholarshipController.getScholarshipsByStudentNumber() 被调用 ===");
        logger.info("查询学生奖学金记录: 学号={}", studentNumber);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsByStudentStudentId(studentNumber);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学号 {} 的 {} 条奖学金记录", studentNumber, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/semester/{semester}")
    @Operation(summary = "根据学期获取奖学金记录", description = "根据学期获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsBySemester(@PathVariable String semester) {
        logger.info("=== ScholarshipController.getScholarshipsBySemester() 被调用 ===");
        logger.info("查询学期奖学金记录: 学期={}", semester);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsBySemester(semester);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学期 {} 的 {} 条奖学金记录", semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学期奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取奖学金记录", description = "根据状态获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsByStatus(@PathVariable Integer status) {
        logger.info("=== ScholarshipController.getScholarshipsByStatus() 被调用 ===");
        logger.info("查询状态奖学金记录: 状态={}", status);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsByStatus(status);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取状态 {} 的 {} 条奖学金记录", status, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取状态奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型获取奖学金记录", description = "根据类型获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsByType(@PathVariable String type) {
        logger.info("=== ScholarshipController.getScholarshipsByType() 被调用 ===");
        logger.info("查询类型奖学金记录: 类型={}", type);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsByType(type);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取类型 {} 的 {} 条奖学金记录", type, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取类型奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/{studentId}/semester/{semester}")
    @Operation(summary = "根据学生ID和学期获取奖学金记录", description = "根据学生ID和学期获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsByStudentAndSemester(
            @PathVariable Long studentId, @PathVariable String semester) {
        logger.info("=== ScholarshipController.getScholarshipsByStudentAndSemester() 被调用 ===");
        logger.info("查询学生学期奖学金记录: 学生ID={}, 学期={}", studentId, semester);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsByStudentAndSemester(studentId, semester);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学生 {} 学期 {} 的 {} 条奖学金记录", studentId, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生学期奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/number/{studentNumber}/semester/{semester}")
    @Operation(summary = "根据学号和学期获取奖学金记录", description = "根据学号和学期获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsByStudentNumberAndSemester(
            @PathVariable String studentNumber, @PathVariable String semester) {
        logger.info("=== ScholarshipController.getScholarshipsByStudentNumberAndSemester() 被调用 ===");
        logger.info("查询学生学期奖学金记录: 学号={}, 学期={}", studentNumber, semester);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsByStudentStudentIdAndSemester(studentNumber, semester);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取学号 {} 学期 {} 的 {} 条奖学金记录", studentNumber, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取学生学期奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/department/{department}/semester/{semester}")
    @Operation(summary = "根据院系和学期获取奖学金记录", description = "根据院系和学期获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsByDepartmentAndSemester(
            @PathVariable String department, @PathVariable String semester) {
        logger.info("=== ScholarshipController.getScholarshipsByDepartmentAndSemester() 被调用 ===");
        logger.info("查询院系学期奖学金记录: 院系={}, 学期={}", department, semester);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsByDepartmentAndSemester(department, semester);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取院系 {} 学期 {} 的 {} 条奖学金记录", department, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取院系学期奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/major/{major}/semester/{semester}")
    @Operation(summary = "根据专业和学期获取奖学金记录", description = "根据专业和学期获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsByMajorAndSemester(
            @PathVariable String major, @PathVariable String semester) {
        logger.info("=== ScholarshipController.getScholarshipsByMajorAndSemester() 被调用 ===");
        logger.info("查询专业学期奖学金记录: 专业={}, 学期={}", major, semester);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsByMajorAndSemester(major, semester);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取专业 {} 学期 {} 的 {} 条奖学金记录", major, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取专业学期奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/grade/{grade}/semester/{semester}")
    @Operation(summary = "根据年级和学期获取奖学金记录", description = "根据年级和学期获取奖学金记录列表")
    public ResponseEntity<List<ScholarshipResponse>> getScholarshipsByGradeAndSemester(
            @PathVariable Integer grade, @PathVariable String semester) {
        logger.info("=== ScholarshipController.getScholarshipsByGradeAndSemester() 被调用 ===");
        logger.info("查询年级学期奖学金记录: 年级={}, 学期={}", grade, semester);

        try {
            List<Scholarship> scholarships = scholarshipService.getScholarshipsByGradeAndSemester(grade, semester);
            
            List<ScholarshipResponse> responses = scholarships.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            logger.info("✅ 成功获取年级 {} 学期 {} 的 {} 条奖学金记录", grade, semester, responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("获取年级学期奖学金记录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics/{semester}")
    @Operation(summary = "获取奖学金统计信息", description = "获取指定学期的奖学金统计信息")
    public ResponseEntity<ScholarshipService.ScholarshipStatistics> getScholarshipStatistics(@PathVariable String semester) {
        logger.info("=== ScholarshipController.getScholarshipStatistics() 被调用 ===");
        logger.info("查询奖学金统计: 学期={}", semester);

        try {
            ScholarshipService.ScholarshipStatistics statistics = scholarshipService.getScholarshipStatistics(semester);
            
            logger.info("✅ 成功获取学期 {} 的奖学金统计信息", semester);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("获取奖学金统计信息失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics/department/{department}/{semester}")
    @Operation(summary = "获取院系奖学金统计信息", description = "获取指定院系和学期的奖学金统计信息")
    public ResponseEntity<ScholarshipService.DepartmentScholarshipStatistics> getDepartmentScholarshipStatistics(
            @PathVariable String department, @PathVariable String semester) {
        logger.info("=== ScholarshipController.getDepartmentScholarshipStatistics() 被调用 ===");
        logger.info("查询院系奖学金统计: 院系={}, 学期={}", department, semester);

        try {
            ScholarshipService.DepartmentScholarshipStatistics statistics = 
                    scholarshipService.getDepartmentScholarshipStatistics(department, semester);
            
            logger.info("✅ 成功获取院系 {} 学期 {} 的奖学金统计信息", department, semester);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("获取院系奖学金统计信息失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 将Scholarship实体转换为ScholarshipResponse DTO
     */
    private ScholarshipResponse convertToResponse(Scholarship scholarship) {
        ScholarshipResponse response = new ScholarshipResponse();
        response.setId(scholarship.getId());
        response.setStudentId(scholarship.getStudent().getId());
        response.setStudentName(scholarship.getStudent().getRealName());
        response.setStudentNumber(scholarship.getStudent().getStudentId());
        response.setAmount(scholarship.getAmount());
        response.setName(scholarship.getName());
        response.setType(scholarship.getType());
        response.setSemester(scholarship.getSemester());
        response.setAwardDate(scholarship.getAwardDate());
        response.setStatus(scholarship.getStatus());
        response.setStatusDescription(getStatusDescription(scholarship.getStatus()));
        response.setDescription(scholarship.getDescription());
        response.setReason(scholarship.getReason());
        response.setCreateTime(scholarship.getCreateTime());
        response.setUpdateTime(scholarship.getUpdateTime());
        return response;
    }

    /**
     * 获取状态描述
     */
    private String getStatusDescription(Integer status) {
        switch (status) {
            case 0:
                return "待审核";
            case 1:
                return "已批准";
            case 2:
                return "已拒绝";
            default:
                return "未知状态";
        }
    }
}