package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.Course;
import com.example.smartcampus.entity.Syllabus;
import com.example.smartcampus.repository.CourseRepository;
import com.example.smartcampus.repository.SyllabusRepository;
import com.example.smartcampus.service.SyllabusService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class SyllabusServiceImpl implements SyllabusService {

    private final SyllabusRepository syllabusRepo;
    private final CourseRepository courseRepo;

    public SyllabusServiceImpl(SyllabusRepository syllabusRepo,
                               CourseRepository courseRepo) {
        this.syllabusRepo = syllabusRepo;
        this.courseRepo = courseRepo;
    }

    @Override
    public Syllabus createOrUpdate(Long courseId, String content) {

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        Syllabus syllabus = syllabusRepo.findByCourseId(courseId)
                .orElseGet(() -> {
                    Syllabus s = new Syllabus();
                    s.setCourse(course);
                    return s;
                });

        syllabus.setContent(content);
        syllabus.setUpdatedAt(LocalDateTime.now());

        return syllabusRepo.save(syllabus);
    }

    @Override
    public Syllabus getByCourse(Long courseId) {
        return syllabusRepo.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("该课程尚未设置教学大纲"));
    }
}
