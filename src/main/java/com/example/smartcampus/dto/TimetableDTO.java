package com.example.smartcampus.dto;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class TimetableDTO {
    private Long id;
    private Long courseId;
    private String courseName;
    private String teacherName;
    private String classroom;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}