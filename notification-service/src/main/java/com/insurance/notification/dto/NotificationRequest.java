package com.insurance.notification.dto;

import com.insurance.notification.entity.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {

    @NotNull(message = "Claim id is required")
    private Long claimId;

    @Email(message = "Invalid recipient email")
    @NotBlank(message = "Recipient email is required")
    private String recipientEmail;

    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Message is required")
    private String message;
}