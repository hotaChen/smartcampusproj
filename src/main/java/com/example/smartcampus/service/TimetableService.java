package com.example.smartcampus.service;

import com.example.smartcampus.dto.TimetableDTO;
import com.example.smartcampus.entity.Timetable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface TimetableService {

    Timetable createTimetable(
            Long courseId,
            Long classroomId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );

    List<TimetableDTO> getCourseTimetable(Long courseId);

    Timetable updateTimetable(Long id, Long classroomId, DayOfWeek dayOfWeek, LocalTime parse, LocalTime parse1);

    void deleteTimetable(Long id);
}

