package com.insurance.notification.consumer;

import com.insurance.notification.config.RabbitMQConfig;
import com.insurance.notification.dto.NotificationRequest;
import com.insurance.notification.entity.NotificationType;
import com.insurance.notification.event.ClaimEvent;
import com.insurance.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClaimEventConsumer {

    private final NotificationService notificationService;

    public ClaimEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMQConfig.CLAIM_NOTIFICATION_QUEUE)
    public void consumeClaimEvent(ClaimEvent event) {
        NotificationRequest request = new NotificationRequest();
        request.setClaimId(event.getClaimId());
        request.setRecipientEmail(event.getRecipientEmail());
        request.setSubject(buildSubject(event));
        request.setMessage(event.getMessage());
        request.setNotificationType(mapNotificationType(event.getEventType(), event.getClaimStatus()));

        notificationService.createNotification(request);
    }

    private String buildSubject(ClaimEvent event) {
        if ("CLAIM_SUBMITTED".equals(event.getEventType())) {
            return "Claim Submitted Successfully";
        }

        if ("CLAIM_STATUS_UPDATED".equals(event.getEventType())) {
            return "Claim Status Updated to " + event.getClaimStatus();
        }

        return "Claim Notification";
    }

    private NotificationType mapNotificationType(String eventType, String claimStatus) {
        if ("CLAIM_SUBMITTED".equals(eventType)) {
            return NotificationType.CLAIM_SUBMITTED;
        }

        if ("CLAIM_STATUS_UPDATED".equals(eventType)) {
            if ("UNDER_REVIEW".equals(claimStatus)) {
                return NotificationType.CLAIM_UNDER_REVIEW;
            }
            if ("APPROVED".equals(claimStatus)) {
                return NotificationType.CLAIM_APPROVED;
            }
            if ("REJECTED".equals(claimStatus)) {
                return NotificationType.CLAIM_REJECTED;
            }
        }

        return NotificationType.GENERAL;
    }
}