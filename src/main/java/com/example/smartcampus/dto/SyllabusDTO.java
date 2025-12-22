package com.example.smartcampus.dto;

import java.time.LocalDateTime;

public class SyllabusDTO {
    private Long id;
    private Long courseId;
    private String content;
    private LocalDateTime updatedAt;

    public SyllabusDTO(Long id, Long courseId, String content, LocalDateTime updatedAt) {
        this.id = id;
        this.courseId = courseId;
        this.content = content;
        this.updatedAt = updatedAt;
    }

    // getter & setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

