package com.example.smartcampus.controller;

import com.example.smartcampus.dto.CreateCourseRequest;
import com.example.smartcampus.dto.CourseDTO;
import com.example.smartcampus.entity.Course;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "课程管理")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    private CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getCourseCode(),
                course.getName(),
                course.getCredit(),
                course.getCapacity(),
                course.getTeacher().getId(),
                course.getTeacherName(),
                course.getClassroom().getId(),
                course.getClassroom().getBuilding() + "-" +
                        course.getClassroom().getRoomNumber()
        );
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Course createCourse(
            @RequestBody CreateCourseRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return courseService.createCourse(request, userDetails);
    }


    @PutMapping("/{id}")
    public Course update(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id,
            @RequestBody Course course
    ) {
        checkAdmin(user);
        return courseService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id
    ) {
        checkAdmin(user);
        courseService.delete(id);
    }

    @GetMapping
    public List<CourseDTO> get() {
        return courseService.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private void checkAdmin(CustomUserDetails user) {
        if (user.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("仅管理员可操作");
        }
    }
}
