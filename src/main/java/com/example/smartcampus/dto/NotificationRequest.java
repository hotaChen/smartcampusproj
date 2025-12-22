package com.example.smartcampus.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationRequest {
    private String title;
    private String content;
    private String type;
    private LocalDateTime expireTime;
    private String targetAudience;
    private Integer priority;
}