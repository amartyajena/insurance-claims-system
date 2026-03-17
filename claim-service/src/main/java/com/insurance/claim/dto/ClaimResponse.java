package com.insurance.claim.dto;

import com.insurance.claim.entity.ClaimStatus;
import com.insurance.claim.entity.ClaimType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ClaimResponse {
    private Long id;
    private String claimNumber;
    private Long customerId;
    private Long policyId;
    private ClaimType claimType;
    private BigDecimal claimAmount;
    private LocalDate incidentDate;
    private String description;
    private ClaimStatus status;
    private LocalDateTime createdAt;
}