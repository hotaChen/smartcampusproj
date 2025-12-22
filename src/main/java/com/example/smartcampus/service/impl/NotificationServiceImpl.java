package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.Notification;
import com.example.smartcampus.repository.NotificationRepository;
import com.example.smartcampus.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Page<Notification> getActiveNotifications(Pageable pageable) {
        return notificationRepository.findByIsActiveTrueOrderByPublishTimeDesc(pageable);
    }

    @Override
    public List<Notification> getByTargetAudience(String audience) {
        return notificationRepository.findByTargetAudienceAndIsActiveTrueOrderByPublishTimeDesc(audience);
    }

    @Override
    public List<Notification> getByType(String type) {
        return notificationRepository.findByTypeAndIsActiveTrueOrderByPublishTimeDesc(type);
    }

    @Override
    public Page<Notification> getByPublisherId(Long publisherId, Pageable pageable) {
        return notificationRepository.findByPublisherIdAndIsActiveTrueOrderByPublishTimeDesc(publisherId, pageable);
    }

    @Override
    public List<Notification> getByPublishTimeBetween(LocalDateTime start, LocalDateTime end) {
        return notificationRepository.findByPublishTimeBetweenAndIsActiveTrue(start, end);
    }

    @Override
    public List<Notification> getCurrentActiveNotifications() {
        LocalDateTime now = LocalDateTime.now();
        return notificationRepository.findActiveNotifications(now);
    }

    @Override
    public List<Notification> getActiveNotificationsForAudiences(List<String> audiences) {
        LocalDateTime now = LocalDateTime.now();
        return notificationRepository.findActiveNotificationsForAudiences(audiences, now);
    }

    @Override
    public Notification createNotification(Notification notification) {
        // 设置默认值
        if (notification.getPublishTime() == null) {
            notification.setPublishTime(LocalDateTime.now());
        }
        if (notification.getIsActive() == null) {
            notification.setIsActive(true);
        }
        if (notification.getPriority() == null) {
            notification.setPriority(3); // 默认优先级
        }
        return notificationRepository.save(notification);
    }

    @Override
    public Notification updateNotification(Long id, Notification notification) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知不存在，ID: " + id));

        // 更新字段
        if (notification.getTitle() != null) {
            existingNotification.setTitle(notification.getTitle());
        }
        if (notification.getContent() != null) {
            existingNotification.setContent(notification.getContent());
        }
        if (notification.getType() != null) {
            existingNotification.setType(notification.getType());
        }
        if (notification.getExpireTime() != null) {
            existingNotification.setExpireTime(notification.getExpireTime());
        }
        if (notification.getTargetAudience() != null) {
            existingNotification.setTargetAudience(notification.getTargetAudience());
        }
        if (notification.getPriority() != null) {
            existingNotification.setPriority(notification.getPriority());
        }
        if (notification.getIsActive() != null) {
            existingNotification.setIsActive(notification.getIsActive());
        }

        return notificationRepository.save(existingNotification);
    }

    @Override
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知不存在，ID: " + id));
        notification.setIsActive(false);
        notificationRepository.save(notification);
    }

    @Override
    public Long getActiveNotificationsCount() {
        return notificationRepository.countByIsActiveTrue();
    }
}