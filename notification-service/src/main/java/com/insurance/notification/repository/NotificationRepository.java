package com.insurance.notification.repository;

import com.insurance.notification.entity.Notification;
import com.insurance.notification.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientEmail(String recipientEmail);
    List<Notification> findByClaimId(Long claimId);
    List<Notification> findByNotificationType(NotificationType notificationType);
    List<Notification> findBySent(boolean sent);
}