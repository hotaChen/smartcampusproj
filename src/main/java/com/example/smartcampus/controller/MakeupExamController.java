package com.example.smartcampus.controller;

import com.example.smartcampus.dto.MakeupExamApprovalRequest;
import com.example.smartcampus.dto.MakeupExamGradeRequest;
import com.example.smartcampus.dto.MakeupExamRequest;
import com.example.smartcampus.dto.MakeupExamScheduleRequest;
import com.example.smartcampus.entity.MakeupExam;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.service.MakeupExamService;
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
@RequestMapping("/api/makeup-exams")
@Tag(name = "补考管理", description = "补考申请、审批、成绩录入相关接口")
public class MakeupExamController {

    private final MakeupExamService makeupExamService;
    private final UserService userService;

    public MakeupExamController(MakeupExamService makeupExamService, UserService userService) {
        this.makeupExamService = makeupExamService;
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
     * 教师/管理员直接安排补考
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "安排补考", description = "教师或管理员直接安排补考")
    public ResponseEntity<?> scheduleMakeupExam(@Valid @RequestBody MakeupExamScheduleRequest request) {
        try {
            MakeupExam makeupExam = makeupExamService.scheduleMakeupExam(request);
            return ResponseEntity.ok(makeupExam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 教师/管理员更新补考记录
     */
    @PutMapping("/{makeupExamId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "更新补考记录", description = "教师或管理员更新补考记录")
    public ResponseEntity<?> updateMakeupExam(@PathVariable Long makeupExamId,
                                               @Valid @RequestBody MakeupExamScheduleRequest request) {
        try {
            MakeupExam makeupExam = makeupExamService.updateMakeupExam(makeupExamId, request);
            return ResponseEntity.ok(makeupExam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 学生申请补考
     */
    @PostMapping("/apply")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "申请补考", description = "学生申请补考")
    public ResponseEntity<?> applyForMakeupExam(@Valid @RequestBody MakeupExamRequest makeupExamRequest) {
        try {
            User currentUser = getCurrentUser();
            MakeupExam makeupExam = makeupExamService.applyForMakeupExam(makeupExamRequest, currentUser.getStudentId());
            return ResponseEntity.ok(makeupExam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 教师审批补考申请
     */
    @PutMapping("/{makeupExamId}/approve")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "审批补考申请", description = "教师审批补考申请")
    public ResponseEntity<?> approveMakeupExam(@PathVariable Long makeupExamId,
                                               @Valid @RequestBody MakeupExamApprovalRequest approvalRequest) {
        try {
            User currentUser = getCurrentUser();
            MakeupExam makeupExam = makeupExamService.approveMakeupExam(makeupExamId, approvalRequest, currentUser.getId());
            return ResponseEntity.ok(makeupExam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 教师录入补考成绩
     */
    @PutMapping("/{makeupExamId}/grade")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "录入补考成绩", description = "教师录入补考成绩")
    public ResponseEntity<?> enterMakeupExamGrade(@PathVariable Long makeupExamId,
                                                   @Valid @RequestBody MakeupExamGradeRequest gradeRequest) {
        try {
            User currentUser = getCurrentUser();
            MakeupExam makeupExam = makeupExamService.enterMakeupExamGrade(makeupExamId, gradeRequest, currentUser.getId());
            return ResponseEntity.ok(makeupExam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根据ID获取补考记录
     */
    @GetMapping("/{makeupExamId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "获取补考记录详情", description = "根据补考记录ID获取详情")
    public ResponseEntity<?> getMakeupExamById(@PathVariable Long makeupExamId) {
        try {
            Optional<MakeupExam> makeupExam = makeupExamService.getMakeupExamById(makeupExamId);
            if (makeupExam.isPresent()) {
                return ResponseEntity.ok(makeupExam.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取学生的所有补考记录
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "获取学生补考记录", description = "根据学生ID获取所有补考记录")
    public ResponseEntity<?> getMakeupExamsByStudentId(@PathVariable Long studentId) {
        try {
            User currentUser = getCurrentUser();
            
            // 学生只能查看自己的补考记录
            if ("STUDENT".equals(currentUser.getUserType()) && !currentUser.getId().equals(studentId)) {
                return ResponseEntity.status(403).body("无权限查看其他学生的补考记录");
            }
            
            List<MakeupExam> makeupExams = makeupExamService.getMakeupExamsByStudentId(studentId);
            return ResponseEntity.ok(makeupExams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根据学号获取学生的所有补考记录
     */
    @GetMapping("/student/number/{studentNumber}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "根据学号获取学生补考记录", description = "根据学号获取所有补考记录")
    public ResponseEntity<?> getMakeupExamsByStudentNumber(@PathVariable String studentNumber) {
        try {
            User currentUser = getCurrentUser();
            
            // 学生只能查看自己的补考记录
            if ("STUDENT".equals(currentUser.getUserType()) && !currentUser.getStudentId().equals(studentNumber)) {
                return ResponseEntity.status(403).body("无权限查看其他学生的补考记录");
            }
            
            List<MakeupExam> makeupExams = makeupExamService.getMakeupExamsByStudentStudentId(studentNumber);
            return ResponseEntity.ok(makeupExams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取教师审批的所有补考记录
     */
    @GetMapping("/teacher")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "获取教师审批的补考记录", description = "获取当前教师审批的所有补考记录")
    public ResponseEntity<?> getMakeupExamsByTeacherId() {
        try {
            User currentUser = getCurrentUser();
            List<MakeupExam> makeupExams = makeupExamService.getMakeupExamsByTeacherId(currentUser.getId());
            return ResponseEntity.ok(makeupExams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取学生在某学期的补考记录
     */
    @GetMapping("/student/{studentId}/semester/{semester}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "获取学生学期补考记录", description = "获取学生在某学期的所有补考记录")
    public ResponseEntity<?> getStudentMakeupExamsBySemester(@PathVariable Long studentId, 
                                                              @PathVariable String semester) {
        try {
            User currentUser = getCurrentUser();
            
            // 学生只能查看自己的补考记录
            if ("STUDENT".equals(currentUser.getUserType()) && !currentUser.getId().equals(studentId)) {
                return ResponseEntity.status(403).body("无权限查看其他学生的补考记录");
            }
            
            List<MakeupExam> makeupExams = makeupExamService.getStudentMakeupExamsBySemester(studentId, semester);
            return ResponseEntity.ok(makeupExams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根据学号获取学生在某学期的补考记录
     */
    @GetMapping("/student/number/{studentNumber}/semester/{semester}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "根据学号获取学生学期补考记录", description = "根据学号获取学生在某学期的所有补考记录")
    public ResponseEntity<?> getStudentMakeupExamsByStudentNumberAndSemester(@PathVariable String studentNumber, 
                                                                              @PathVariable String semester) {
        try {
            User currentUser = getCurrentUser();
            
            // 学生只能查看自己的补考记录
            if ("STUDENT".equals(currentUser.getUserType()) && !currentUser.getStudentId().equals(studentNumber)) {
                return ResponseEntity.status(403).body("无权限查看其他学生的补考记录");
            }
            
            List<MakeupExam> makeupExams = makeupExamService.getStudentMakeupExamsByStudentIdAndSemester(studentNumber, semester);
            return ResponseEntity.ok(makeupExams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取教师教授的某门课程在某学期的所有补考记录
     */
    @GetMapping("/teacher/course/{courseCode}/semester/{semester}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "获取课程补考记录", description = "获取教师教授的某门课程在某学期的所有补考记录")
    public ResponseEntity<?> getTeacherCourseMakeupExamsBySemester(@PathVariable String courseCode, 
                                                                   @PathVariable String semester) {
        try {
            User currentUser = getCurrentUser();
            List<MakeupExam> makeupExams = makeupExamService.getTeacherCourseMakeupExamsBySemester(
                    currentUser.getId(), courseCode, semester);
            return ResponseEntity.ok(makeupExams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取所有待审批的补考申请
     */
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "获取待审批补考申请", description = "获取所有待审批的补考申请")
    public ResponseEntity<?> getPendingMakeupExams() {
        try {
            List<MakeupExam> makeupExams = makeupExamService.getPendingMakeupExams();
            return ResponseEntity.ok(makeupExams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 删除补考记录
     */
    @DeleteMapping("/{makeupExamId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "删除补考记录", description = "教师或管理员删除补考记录")
    public ResponseEntity<?> deleteMakeupExam(@PathVariable Long makeupExamId) {
        try {
            User currentUser = getCurrentUser();
            boolean result = makeupExamService.deleteMakeupExam(makeupExamId, currentUser.getId());
            return ResponseEntity.ok(result ? "补考记录删除成功" : "补考记录删除失败");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取所有补考记录（支持按学号、课程代码、学期查询）
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "获取补考记录列表", description = "获取所有补考记录，支持按学号、课程代码、学期查询")
    public ResponseEntity<?> getAllMakeupExams(
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) String courseCode,
            @RequestParam(required = false) String semester) {
        try {
            List<MakeupExam> makeupExams;
            
            if (studentNumber != null) {
                // 按学号查询
                makeupExams = makeupExamService.getMakeupExamsByStudentStudentId(studentNumber);
            } else if (courseCode != null && semester != null) {
                // 按课程代码和学期查询
                makeupExams = makeupExamService.getMakeupExamsByCourseCodeAndSemester(courseCode, semester);
            } else {
                // 获取所有补考记录
                makeupExams = makeupExamService.getAllMakeupExams();
            }
            
            return ResponseEntity.ok(makeupExams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取当前登录学生的补考记录
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "获取我的补考记录", description = "获取当前登录学生的所有补考记录")
    public ResponseEntity<?> getMyMakeupExams() {
        try {
            User currentUser = getCurrentUser();
            List<MakeupExam> makeupExams = makeupExamService.getMakeupExamsByStudentStudentId(currentUser.getStudentId());
            return ResponseEntity.ok(makeupExams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}