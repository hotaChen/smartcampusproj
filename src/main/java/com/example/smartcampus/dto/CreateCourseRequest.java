package com.example.smartcampus.dto;

import lombok.Data;

@Data
public class CreateCourseRequest {

    private String courseCode;
    private String name;
    private Integer credit;
    private Integer capacity;
    private Long classroomId;

    // 只有 ADMIN 用
    private Long teacherId;
}

