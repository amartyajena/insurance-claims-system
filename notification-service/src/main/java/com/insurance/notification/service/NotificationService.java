package com.insurance.notification.service;

import com.insurance.notification.dto.NotificationRequest;
import com.insurance.notification.dto.NotificationResponse;
import com.insurance.notification.entity.Notification;
import com.insurance.notification.entity.NotificationType;
import com.insurance.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .claimId(request.getClaimId())
                .recipientEmail(request.getRecipientEmail())
                .notificationType(request.getNotificationType())
                .subject(request.getSubject())
                .message(request.getMessage())
                .sent(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);
        return mapToResponse(saved);
    }

    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return mapToResponse(notification);
    }

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<NotificationResponse> getNotificationsByRecipientEmail(String email) {
        return notificationRepository.findByRecipientEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<NotificationResponse> getNotificationsByClaimId(Long claimId) {
        return notificationRepository.findByClaimId(claimId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<NotificationResponse> getNotificationsByType(NotificationType type) {
        return notificationRepository.findByNotificationType(type)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<NotificationResponse> getNotificationsBySentStatus(boolean sent) {
        return notificationRepository.findBySent(sent)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public NotificationResponse markAsSent(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notification.setSent(true);
        notification.setSentAt(LocalDateTime.now());

        Notification updated = notificationRepository.save(notification);
        return mapToResponse(updated);
    }

    public String deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notificationRepository.delete(notification);
        return "Notification deleted successfully";
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .claimId(notification.getClaimId())
                .recipientEmail(notification.getRecipientEmail())
                .notificationType(notification.getNotificationType())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .sent(notification.isSent())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .build();
    }
}