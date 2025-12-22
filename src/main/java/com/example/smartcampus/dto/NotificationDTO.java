package com.example.smartcampus.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String title;
    private String content;
    private String type;
    private Long publisherId;
    private String publisherName;
    private LocalDateTime publishTime;
    private LocalDateTime expireTime;
    private String targetAudience;
    private Integer priority;
    private Boolean isActive;
    private LocalDateTime createTime;
}