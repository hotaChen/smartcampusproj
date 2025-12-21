package com.example.smartcampus.service;

import com.example.smartcampus.entity.Syllabus;

public interface SyllabusService {

    Syllabus createOrUpdate(Long courseId, String content);

    Syllabus getByCourse(Long courseId);

    void deleteByCourseId(Long courseId);
}
