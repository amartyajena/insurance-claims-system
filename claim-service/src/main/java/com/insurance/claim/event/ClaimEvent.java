package com.insurance.claim.event;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimEvent {
    private Long claimId;
    private String claimNumber;
    private Long customerId;
    private Long policyId;
    private String recipientEmail;
    private String eventType;
    private String claimStatus;
    private String message;
}