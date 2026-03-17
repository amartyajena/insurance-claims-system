package com.insurance.claim.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PolicyExternalResponse {
    private Long id;
    private String policyNumber;
    private Long customerId;
    private String policyType;
    private BigDecimal coverageAmount;
    private BigDecimal premiumAmount;
    private String startDate;
    private String endDate;
    private String status;
    private boolean active;
}