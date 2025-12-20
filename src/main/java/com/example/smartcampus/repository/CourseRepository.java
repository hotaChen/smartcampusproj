package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
