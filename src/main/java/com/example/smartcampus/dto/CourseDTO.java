package com.example.smartcampus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseDTO {

    private Long id;
    private String courseCode;
    private String name;
    private Integer credit;
    private Integer capacity;
    private Long teacherId;
    private String teacherName;
    private Long classroomId;
    private String classroomName;
}
