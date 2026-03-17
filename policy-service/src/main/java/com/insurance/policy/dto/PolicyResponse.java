package com.insurance.policy.dto;

import com.insurance.policy.entity.PolicyStatus;
import com.insurance.policy.entity.PolicyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class PolicyResponse {
    private Long id;
    private String policyNumber;
    private Long customerId;
    private PolicyType policyType;
    private BigDecimal coverageAmount;
    private BigDecimal premiumAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private PolicyStatus status;
    private boolean active;
}