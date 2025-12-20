package com.example.smartcampus.entity;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "syllabus")
@Getter
@Setter
public class Syllabus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id", nullable = false, unique = true)
    private Course course;

    @Lob
    @Column(nullable = false)
    private String content;

    private LocalDateTime updatedAt;
}
