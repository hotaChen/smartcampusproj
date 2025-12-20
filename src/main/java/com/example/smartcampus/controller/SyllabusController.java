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

    @PostMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public SyllabusDTO createOrUpdate(
            @PathVariable Long courseId,
            @RequestBody String content) {
        Syllabus syllabus = syllabusService.createOrUpdate(courseId, content);
        return new SyllabusDTO(
                syllabus.getId(),
                syllabus.getCourse().getId(),
                syllabus.getContent(),
                syllabus.getUpdatedAt()
        );
    }

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
}
