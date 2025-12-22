package com.example.smartcampus.service;

import com.example.smartcampus.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {

    // 获取所有活跃通知（分页）
    Page<Notification> getActiveNotifications(Pageable pageable);

    // 根据目标受众获取通知
    List<Notification> getByTargetAudience(String audience);

    // 根据类型获取通知
    List<Notification> getByType(String type);

    // 根据发布者ID获取通知
    Page<Notification> getByPublisherId(Long publisherId, Pageable pageable);

    // 根据发布时间范围获取通知
    List<Notification> getByPublishTimeBetween(LocalDateTime start, LocalDateTime end);

    // 获取当前活跃的通知
    List<Notification> getCurrentActiveNotifications();

    // 为特定受众获取活跃通知
    List<Notification> getActiveNotificationsForAudiences(List<String> audiences);

    // 创建通知
    Notification createNotification(Notification notification);

    // 更新通知
    Notification updateNotification(Long id, Notification notification);

    // 删除通知
    void deleteNotification(Long id);

    // 获取活跃通知数量
    Long getActiveNotificationsCount();
}