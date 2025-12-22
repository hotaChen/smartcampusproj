package com.example.smartcampus.controller;

import com.example.smartcampus.dto.GradeEntryRequest;
import com.example.smartcampus.dto.GradeReportDTO;
import com.example.smartcampus.entity.Grade;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.service.GradeService;
import com.example.smartcampus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "成绩管理", description = "成绩录入、查询、修改、删除相关接口")
public class GradeController {

    private final GradeService gradeService;
    private final UserService userService;

    public GradeController(GradeService gradeService, UserService userService) {
        this.gradeService = gradeService;
        this.userService = userService;
    }

    /**
     * 获取当前登录用户
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 录入成绩
     */
    @PostMapping("/enter")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "录入成绩", description = "教师或管理员录入学生成绩")
    public ResponseEntity<?> enterGrade(@Valid @RequestBody GradeEntryRequest gradeRequest) {
        try {
            User currentUser = getCurrentUser();
            Grade grade = gradeService.enterGrade(gradeRequest, currentUser.getId());
            return ResponseEntity.ok(grade);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 更新成绩
     */
    @PutMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "更新成绩", description = "教师或管理员更新学生成绩")
    public ResponseEntity<?> updateGrade(@PathVariable Long gradeId, 
                                         @Valid @RequestBody GradeEntryRequest gradeRequest) {
        try {
            User currentUser = getCurrentUser();
            Grade grade = gradeService.updateGrade(gradeId, gradeRequest, currentUser.getId());
            return ResponseEntity.ok(grade);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根据ID获取成绩
     */
    @GetMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "获取成绩详情", description = "根据成绩ID获取成绩详情")
    public ResponseEntity<?> getGradeById(@PathVariable Long gradeId) {
        try {
            Optional<Grade> grade = gradeService.getGradeById(gradeId);
            if (grade.isPresent()) {
                return ResponseEntity.ok(grade.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取学生的所有成绩
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "获取学生成绩", description = "根据学生ID获取所有成绩")
    public ResponseEntity<?> getGradesByStudentId(@PathVariable Long studentId) {
        try {
            User currentUser = getCurrentUser();
            
            // 学生只能查看自己的成绩
            if ("STUDENT".equals(currentUser.getUserType()) && !currentUser.getId().equals(studentId)) {
                return ResponseEntity.status(403).body("无权限查看其他学生的成绩");
            }
            
            List<Grade> grades = gradeService.getGradesByStudentId(studentId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取教师录入的所有成绩
     */
    @GetMapping("/teacher")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "获取教师录入的成绩", description = "获取当前教师录入的所有成绩")
    public ResponseEntity<?> getGradesByTeacherId() {
        try {
            User currentUser = getCurrentUser();
            List<Grade> grades = gradeService.getGradesByTeacherId(currentUser.getId());
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取学生在某学期的成绩
     */
    @GetMapping("/student/{studentId}/semester/{semester}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "获取学生学期成绩", description = "获取学生在某学期的所有成绩")
    public ResponseEntity<?> getStudentGradesBySemester(@PathVariable Long studentId, 
                                                        @PathVariable String semester) {
        try {
            User currentUser = getCurrentUser();
            
            // 学生只能查看自己的成绩
            if ("STUDENT".equals(currentUser.getUserType()) && !currentUser.getId().equals(studentId)) {
                return ResponseEntity.status(403).body("无权限查看其他学生的成绩");
            }
            
            List<Grade> grades = gradeService.getStudentGradesBySemester(studentId, semester);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取教师教授的某门课程在某学期的所有成绩
     */
    @GetMapping("/teacher/course/{courseCode}/semester/{semester}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "获取课程成绩", description = "获取教师教授的某门课程在某学期的所有成绩")
    public ResponseEntity<?> getTeacherCourseGradesBySemester(@PathVariable String courseCode, 
                                                              @PathVariable String semester) {
        try {
            User currentUser = getCurrentUser();
            List<Grade> grades = gradeService.getTeacherCourseGradesBySemester(
                    currentUser.getId(), courseCode, semester);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 删除成绩
     */
    @DeleteMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "删除成绩", description = "教师或管理员删除成绩")
    public ResponseEntity<?> deleteGrade(@PathVariable Long gradeId) {
        try {
            User currentUser = getCurrentUser();
            boolean result = gradeService.deleteGrade(gradeId, currentUser.getId());
            return ResponseEntity.ok(result ? "成绩删除成功" : "成绩删除失败");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 批量录入成绩
     */
    @PostMapping("/batch")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "批量录入成绩", description = "教师或管理员批量录入学生成绩")
    public ResponseEntity<?> batchEnterGrades(@Valid @RequestBody List<GradeEntryRequest> gradeRequests) {
        try {
            User currentUser = getCurrentUser();
            List<Grade> grades = gradeService.batchEnterGrades(gradeRequests, currentUser.getId());
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 计算学生的平均分
     */
    @GetMapping("/student/{studentId}/average/{semester}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "计算学生平均分", description = "计算学生在某学期的平均分")
    public ResponseEntity<?> calculateAverageScoreByStudentAndSemester(@PathVariable Long studentId, 
                                                                      @PathVariable String semester) {
        try {
            User currentUser = getCurrentUser();
            
            // 学生只能查看自己的成绩
            if ("STUDENT".equals(currentUser.getUserType()) && !currentUser.getId().equals(studentId)) {
                return ResponseEntity.status(403).body("无权限查看其他学生的成绩");
            }
            
            Float averageScore = gradeService.calculateAverageScoreByStudentAndSemester(studentId, semester);
            return ResponseEntity.ok(averageScore);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 计算课程的平均分
     */
    @GetMapping("/course/{courseCode}/average/{semester}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "计算课程平均分", description = "计算课程在某学期的平均分")
    public ResponseEntity<?> calculateAverageScoreByCourseAndSemester(@PathVariable String courseCode, 
                                                                      @PathVariable String semester) {
        try {
            Float averageScore = gradeService.calculateAverageScoreByCourseAndSemester(courseCode, semester);
            return ResponseEntity.ok(averageScore);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 生成学生成绩单
     */
    @GetMapping("/report/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN') or hasRole('STUDENT')")
    @Operation(summary = "生成学生成绩单", description = "生成学生的成绩单，包含成绩统计信息")
    public ResponseEntity<?> generateGradeReport(@PathVariable Long studentId,
                                                 @RequestParam(required = false) String semester) {
        try {
            User currentUser = getCurrentUser();
            
            // 学生只能查看自己的成绩单
            if ("STUDENT".equals(currentUser.getUserType()) && !currentUser.getId().equals(studentId)) {
                return ResponseEntity.status(403).body("无权限查看其他学生的成绩单");
            }
            
            GradeReportDTO report = gradeService.generateGradeReport(studentId, semester);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 生成当前登录学生的成绩单
     */
    @GetMapping("/report/my")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "生成我的成绩单", description = "生成当前登录学生的成绩单")
    public ResponseEntity<?> generateMyGradeReport(@RequestParam(required = false) String semester) {
        try {
            User currentUser = getCurrentUser();
            GradeReportDTO report = gradeService.generateGradeReport(currentUser.getId(), semester);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}