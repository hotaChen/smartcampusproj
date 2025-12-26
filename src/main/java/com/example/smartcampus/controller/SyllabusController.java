package com.example.smartcampus.controller;

import com.example.smartcampus.dto.SyllabusDTO;
import com.example.smartcampus.entity.Syllabus;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.SyllabusService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/syllabus")
public class SyllabusController {

    private final SyllabusService syllabusService;

    public SyllabusController(SyllabusService syllabusService) {
        this.syllabusService = syllabusService;
    }

    /**
     * 创建或更新教学大纲（教师 / 管理员）
     */
    @PutMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public SyllabusDTO createOrUpdate(
            @PathVariable Long courseId,
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String content = request.get("content");
        if (content == null) {
            throw new RuntimeException("大纲内容不能为空");
        }
        // 如果是教师
        if ("TEACHER".equals(userDetails.getUserType())) {
            boolean isTeacherOfCourse = syllabusService.isTeacherOfCourse(userDetails.getUserId(), courseId);
            if (!isTeacherOfCourse) {
                throw new RuntimeException("您无权修改该课程的大纲");
            }
        }

        Syllabus syllabus = syllabusService.createOrUpdate(courseId, content);
        return new SyllabusDTO(
                syllabus.getId(),
                syllabus.getCourse().getId(),
                syllabus.getContent(),
                syllabus.getUpdatedAt()
        );
    }

    /**
     * 查看教学大纲（所有已登录用户）
     */
    @GetMapping("/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public SyllabusDTO get(@PathVariable Long courseId) {
        Syllabus syllabus = syllabusService.getByCourse(courseId);
        return new SyllabusDTO(
                syllabus.getId(),
                syllabus.getCourse().getId(),
                syllabus.getContent(),
                syllabus.getUpdatedAt()
        );
    }

    /**
     * 删除教学大纲（教师 / 管理员）
     */
    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public void delete(@PathVariable Long courseId) {
        syllabusService.deleteByCourseId(courseId);
    }
}
