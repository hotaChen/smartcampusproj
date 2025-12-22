package com.example.smartcampus.controller;

import com.example.smartcampus.dto.TimetableDTO;
import com.example.smartcampus.entity.Timetable;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.TeacherTimetableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teacher/timetable")
@Tag(name = "教师课表")
public class TeacherTimetableController {

    private final TeacherTimetableService timetableService;

    public TeacherTimetableController(TeacherTimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping("/me")
    public List<TimetableDTO> myTimetable(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return timetableService.getMyTimetable(userDetails.getUserId());
    }
}

