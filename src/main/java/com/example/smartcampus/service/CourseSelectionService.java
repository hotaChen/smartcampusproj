package com.example.smartcampus.service;

import com.example.smartcampus.dto.CourseDTO;
import com.example.smartcampus.entity.Course;

import java.util.List;

public interface CourseSelectionService {

    void selectCourse(Long studentId, Long courseId);

    void dropCourse(Long studentId, Long courseId);

    List<CourseDTO> getMyCourses(Long studentId);
}

