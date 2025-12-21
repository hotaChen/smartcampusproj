package com.example.smartcampus.controller;

import com.example.smartcampus.dto.TimetableDTO;
import com.example.smartcampus.entity.Timetable;
import com.example.smartcampus.service.TimetableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/timetables")
@Tag(name = "课程表管理")
public class TimetableController {

    private final TimetableService service;

    public TimetableController(TimetableService service) {
        this.service = service;
    }

    /** 排课（管理员 / 教务） */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Timetable create(
            @RequestParam Long courseId,
            @RequestParam Long classroomId,
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam String startTime,
            @RequestParam String endTime
    ) {
        return service.createTimetable(
                courseId,
                classroomId,
                dayOfWeek,
                LocalTime.parse(startTime),
                LocalTime.parse(endTime)
        );
    }

    /** 查看课程课表 */
    @GetMapping("/course/{courseId}")
    public List<TimetableDTO> courseTimetable(@PathVariable Long courseId) {
        return service.getCourseTimetable(courseId);
    }

}

