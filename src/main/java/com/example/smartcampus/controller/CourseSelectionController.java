package com.example.smartcampus.controller;

import com.example.smartcampus.dto.CourseDTO;
import com.example.smartcampus.entity.Course;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.CourseSelectionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-selections")
@Tag(name = "选课系统")
public class CourseSelectionController {

    private final CourseSelectionService service;

    public CourseSelectionController(CourseSelectionService service) {
        this.service = service;
    }

    /** 学生选课 */
    @PostMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public void selectCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal CustomUserDetails user) {

        service.selectCourse(user.getUserId(), courseId);
    }

    /** 学生退课 */
    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public void dropCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal CustomUserDetails user) {

        service.dropCourse(user.getUserId(), courseId);
    }

    /** 查看我的课程 */
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public List<CourseDTO> myCourses(
            @AuthenticationPrincipal CustomUserDetails user) {

        return service.getMyCourses(user.getUserId());
    }
}

