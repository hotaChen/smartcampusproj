package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByIsActiveTrueOrderByPublishTimeDesc(Pageable pageable);

    List<Notification> findByTargetAudienceAndIsActiveTrueOrderByPublishTimeDesc(String targetAudience);

    List<Notification> findByTypeAndIsActiveTrueOrderByPublishTimeDesc(String type);

    Page<Notification> findByPublisherIdAndIsActiveTrueOrderByPublishTimeDesc(Long publisherId, Pageable pageable);

    List<Notification> findByPublishTimeBetweenAndIsActiveTrue(LocalDateTime start, LocalDateTime end);

    @Query("SELECT n FROM Notification n WHERE n.isActive = true AND " +
            "(n.expireTime IS NULL OR n.expireTime > :currentTime) " +
            "ORDER BY n.priority DESC, n.publishTime DESC")
    List<Notification> findActiveNotifications(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT n FROM Notification n WHERE n.isActive = true AND " +
            "n.targetAudience IN (:audiences) AND " +
            "(n.expireTime IS NULL OR n.expireTime > :currentTime) " +
            "ORDER BY n.priority DESC, n.publishTime DESC")
    List<Notification> findActiveNotificationsForAudiences(
            @Param("audiences") List<String> audiences,
            @Param("currentTime") LocalDateTime currentTime);

    Long countByIsActiveTrue();

}