package com.insurance.notification.dto;

import com.insurance.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private Long claimId;
    private String recipientEmail;
    private NotificationType notificationType;
    private String subject;
    private String message;
    private boolean sent;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}