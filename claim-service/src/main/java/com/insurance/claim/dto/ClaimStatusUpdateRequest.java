package com.insurance.claim.dto;

import com.insurance.claim.entity.ClaimStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClaimStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private ClaimStatus status;
}