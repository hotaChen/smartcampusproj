package com.example.smartcampus.controller;

import com.example.smartcampus.dto.TimetableDTO;
import com.example.smartcampus.entity.Course;
import com.example.smartcampus.entity.Timetable;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.TimetableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /** 删除排课（管理员 / 教务） */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.deleteTimetable(id);
    }

    /** 查看课程课表 */
    @GetMapping("/course/{courseId}")
    public List<TimetableDTO> courseTimetable(@PathVariable Long courseId) {
        return service.getCourseTimetable(courseId);
    }

    /** 按课程名称查询课表 */
    @GetMapping("/course/name")
    public List<TimetableDTO> courseTimetableByName(@RequestParam String name) {
        return service.getTimetableByCourseName(name);
    }

    /** 查询所有排课 */
    @GetMapping
    public List<TimetableDTO> getAllTimetables() {
        return service.getAllTimetables();
    }
}


