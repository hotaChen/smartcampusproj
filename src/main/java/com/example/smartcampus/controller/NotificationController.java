package com.example.smartcampus.controller;

import com.example.smartcampus.entity.Notification;
import com.example.smartcampus.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 获取所有活跃通知（分页）
    @GetMapping
    public ResponseEntity<Page<Notification>> getActiveNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.getActiveNotifications(pageable);
        return ResponseEntity.ok(notifications);
    }

    // 根据目标受众获取通知
    @GetMapping("/audience/{audience}")
    public ResponseEntity<List<Notification>> getNotificationsByAudience(
            @PathVariable String audience) {
        List<Notification> notifications = notificationService.getByTargetAudience(audience);
        return ResponseEntity.ok(notifications);
    }

    // 根据类型获取通知
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> getNotificationsByType(@PathVariable String type) {
        List<Notification> notifications = notificationService.getByType(type);
        return ResponseEntity.ok(notifications);
    }

    // 获取发布者的通知
    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<Page<Notification>> getNotificationsByPublisher(
            @PathVariable Long publisherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.getByPublisherId(publisherId, pageable);
        return ResponseEntity.ok(notifications);
    }

    // 获取时间范围内的通知
    @GetMapping("/time-range")
    public ResponseEntity<List<Notification>> getNotificationsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Notification> notifications = notificationService.getByPublishTimeBetween(start, end);
        return ResponseEntity.ok(notifications);
    }

    // 获取当前活跃的通知
    @GetMapping("/active")
    public ResponseEntity<List<Notification>> getCurrentActiveNotifications() {
        List<Notification> notifications = notificationService.getCurrentActiveNotifications();
        return ResponseEntity.ok(notifications);
    }

    // 为特定受众获取活跃通知
    @PostMapping("/active/audiences")
    public ResponseEntity<List<Notification>> getActiveNotificationsForAudiences(
            @RequestBody List<String> audiences) {
        List<Notification> notifications = notificationService.getActiveNotificationsForAudiences(audiences);
        return ResponseEntity.ok(notifications);
    }

    // 创建新通知
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(savedNotification);
    }

    // 更新通知
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(
            @PathVariable Long id, @RequestBody Notification notification) {
        Notification updatedNotification = notificationService.updateNotification(id, notification);
        return ResponseEntity.ok(updatedNotification);
    }

    // 删除通知（软删除）
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }

    // 获取活跃通知数量
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveNotificationsCount() {
        Long count = notificationService.getActiveNotificationsCount();
        return ResponseEntity.ok(count);
    }
}