package com.example.smartcampus.repository;

import com.example.smartcampus.entity.CourseSelection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseSelectionRepository
        extends JpaRepository<CourseSelection, Long> {

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    List<CourseSelection> findByStudentId(Long studentId);

    long countByCourseId(Long courseId);

    Optional<CourseSelection> findByStudentIdAndCourseId(Long studentId, Long courseId);
}

