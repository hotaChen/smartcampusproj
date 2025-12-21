package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {

    Optional<Syllabus> findByCourseId(Long courseId);

    boolean existsByCourseId(Long courseId);
}
