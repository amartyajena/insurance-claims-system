package com.insurance.audit.service;

import com.insurance.audit.dto.AuditLogResponse;
import com.insurance.audit.entity.AuditLog;
import com.insurance.audit.event.ClaimEvent;
import com.insurance.audit.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void saveAuditLog(ClaimEvent event) {
        AuditLog log = AuditLog.builder()
                .claimId(event.getClaimId())
                .claimNumber(event.getClaimNumber())
                .customerId(event.getCustomerId())
                .policyId(event.getPolicyId())
                .eventType(event.getEventType())
                .claimStatus(event.getClaimStatus())
                .message(event.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    public List<AuditLogResponse> getAllLogs() {
        return auditLogRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AuditLogResponse> getLogsByClaimId(Long claimId) {
        return auditLogRepository.findByClaimId(claimId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AuditLogResponse> getLogsByEventType(String eventType) {
        return auditLogRepository.findByEventType(eventType)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AuditLogResponse> getLogsByClaimStatus(String claimStatus) {
        return auditLogRepository.findByClaimStatus(claimStatus)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AuditLogResponse mapToResponse(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .claimId(log.getClaimId())
                .claimNumber(log.getClaimNumber())
                .customerId(log.getCustomerId())
                .policyId(log.getPolicyId())
                .eventType(log.getEventType())
                .claimStatus(log.getClaimStatus())
                .message(log.getMessage())
                .createdAt(log.getCreatedAt())
                .build();
    }
}