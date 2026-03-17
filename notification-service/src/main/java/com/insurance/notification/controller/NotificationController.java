package com.insurance.notification.controller;

import com.insurance.notification.dto.NotificationRequest;
import com.insurance.notification.dto.NotificationResponse;
import com.insurance.notification.entity.NotificationType;
import com.insurance.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByRecipientEmail(@PathVariable String email) {
        return ResponseEntity.ok(notificationService.getNotificationsByRecipientEmail(email));
    }

    @GetMapping("/claim/{claimId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByClaimId(@PathVariable Long claimId) {
        return ResponseEntity.ok(notificationService.getNotificationsByClaimId(claimId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByType(@PathVariable NotificationType type) {
        return ResponseEntity.ok(notificationService.getNotificationsByType(type));
    }

    @GetMapping("/sent/{sent}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsBySentStatus(@PathVariable boolean sent) {
        return ResponseEntity.ok(notificationService.getNotificationsBySentStatus(sent));
    }

    @PatchMapping("/{id}/mark-sent")
    public ResponseEntity<NotificationResponse> markAsSent(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsSent(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.deleteNotification(id));
    }
}