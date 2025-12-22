package com.example.smartcampus.controller;

import com.example.smartcampus.dto.SyllabusDTO;
import com.example.smartcampus.entity.Syllabus;
import com.example.smartcampus.service.SyllabusService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public SyllabusDTO createOrUpdate(
            @PathVariable Long courseId,
            @RequestBody String content
    ) {
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
