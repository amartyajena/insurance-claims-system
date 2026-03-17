package com.insurance.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
    public class AuditLogResponse {
    private Long id;
    private Long claimId;
    private String claimNumber;
    private Long customerId;
    private Long policyId;
    private String eventType;
    private String claimStatus;
    private String message;
    private LocalDateTime createdAt;
}