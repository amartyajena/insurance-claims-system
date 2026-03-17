package com.insurance.audit.controller;

import com.insurance.audit.dto.AuditLogResponse;
import com.insurance.audit.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAllLogs() {
        return ResponseEntity.ok(auditService.getAllLogs());
    }

    @GetMapping("/claim/{claimId}")
    public ResponseEntity<List<AuditLogResponse>> getLogsByClaimId(@PathVariable Long claimId) {
        return ResponseEntity.ok(auditService.getLogsByClaimId(claimId));
    }

    @GetMapping("/event/{eventType}")
    public ResponseEntity<List<AuditLogResponse>> getLogsByEventType(@PathVariable String eventType) {
        return ResponseEntity.ok(auditService.getLogsByEventType(eventType));
    }

    @GetMapping("/status/{claimStatus}")
    public ResponseEntity<List<AuditLogResponse>> getLogsByClaimStatus(@PathVariable String claimStatus) {
        return ResponseEntity.ok(auditService.getLogsByClaimStatus(claimStatus));
    }
}