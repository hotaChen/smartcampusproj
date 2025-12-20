package com.example.smartcampus.service;

import com.example.smartcampus.dto.CreateCourseRequest;
import com.example.smartcampus.entity.Course;
import com.example.smartcampus.security.CustomUserDetails;

import java.util.List;

public interface CourseService {



    Course createCourse(CreateCourseRequest req, CustomUserDetails userDetails);

    Course update(Long id, Course course);

    void delete(Long id);

    List<Course> findAll();
}

