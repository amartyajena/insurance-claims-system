package com.insurance.claim.dto;

import com.insurance.claim.entity.ClaimType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ClaimRequest {

    @NotBlank(message = "Claim number is required")
    private String claimNumber;

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Policy id is required")
    private Long policyId;

    @NotNull(message = "Claim type is required")
    private ClaimType claimType;

    @NotNull(message = "Claim amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Claim amount must be greater than 0")
    private BigDecimal claimAmount;

    @NotNull(message = "Incident date is required")
    private LocalDate incidentDate;

    @NotBlank(message = "Description is required")
    private String description;
}