package com.example.smartcampus.controller;

import com.example.smartcampus.entity.Classroom;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.ClassroomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
@Tag(name = "教室管理")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PostMapping
    public Classroom create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody Classroom classroom
    ) {
        checkAdmin(user);
        return classroomService.create(classroom);
    }

    @PutMapping("/{id}")
    public Classroom update(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id,
            @RequestBody Classroom classroom
    ) {
        checkAdmin(user);
        return classroomService.update(id, classroom);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id
    ) {
        checkAdmin(user);
        classroomService.delete(id);
    }

    @GetMapping
    public List<Classroom> list() {
        return classroomService.findAll();
    }

    private void checkAdmin(CustomUserDetails user) {
        if (user.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("仅管理员可操作");
        }
    }
}

