package com.example.smartcampus.controller;

import com.example.smartcampus.dto.TimetableDTO;
import com.example.smartcampus.entity.Timetable;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.StudentTimetableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/timetable")
@Tag(name = "学生课表")
public class StudentTimetableController {

    private final StudentTimetableService timetableService;

    public StudentTimetableController(
            StudentTimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public List<TimetableDTO> myTimetable(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return timetableService.getMyTimetable(
                userDetails.getUserId()
        );
    }
}

