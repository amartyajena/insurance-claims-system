package com.insurance.claim.service;

import com.insurance.claim.client.CustomerServiceClient;
import com.insurance.claim.client.PolicyServiceClient;
import com.insurance.claim.dto.*;
import com.insurance.claim.entity.Claim;
import com.insurance.claim.entity.ClaimStatus;
import com.insurance.claim.event.ClaimEvent;
import com.insurance.claim.producer.ClaimEventProducer;
import com.insurance.claim.repository.ClaimRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final CustomerServiceClient customerServiceClient;
    private final PolicyServiceClient policyServiceClient;
    private final ClaimEventProducer claimEventProducer;

    public ClaimService(ClaimRepository claimRepository,
                        CustomerServiceClient customerServiceClient,
                        PolicyServiceClient policyServiceClient,
                        ClaimEventProducer claimEventProducer) {
        this.claimRepository = claimRepository;
        this.customerServiceClient = customerServiceClient;
        this.policyServiceClient = policyServiceClient;
        this.claimEventProducer = claimEventProducer;
    }

    public ClaimResponse createClaim(ClaimRequest request, String authHeader) {
        if (claimRepository.existsByClaimNumber(request.getClaimNumber())) {
            throw new RuntimeException("Claim number already exists");
        }

        CustomerExternalResponse customer = validateCustomerAndPolicy(request, authHeader);

        Claim claim = Claim.builder()
                .claimNumber(request.getClaimNumber())
                .customerId(request.getCustomerId())
                .policyId(request.getPolicyId())
                .claimType(request.getClaimType())
                .claimAmount(request.getClaimAmount())
                .incidentDate(request.getIncidentDate())
                .description(request.getDescription())
                .status(ClaimStatus.SUBMITTED)
                .createdAt(LocalDateTime.now())
                .build();

        Claim saved = claimRepository.save(claim);

        ClaimEvent event = ClaimEvent.builder()
                .claimId(saved.getId())
                .claimNumber(saved.getClaimNumber())
                .customerId(saved.getCustomerId())
                .policyId(saved.getPolicyId())
                .recipientEmail(customer.getEmail())
                .eventType("CLAIM_SUBMITTED")
                .claimStatus(saved.getStatus().name())
                .message("Your claim " + saved.getClaimNumber() + " has been submitted successfully.")
                .build();

        claimEventProducer.publishClaimEvent(event);

        return mapToResponse(saved);
    }

    public ClaimResponse getClaimById(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with id: " + id));
        return mapToResponse(claim);
    }

    public ClaimResponse getClaimByClaimNumber(String claimNumber) {
        Claim claim = claimRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new RuntimeException("Claim not found with claim number: " + claimNumber));
        return mapToResponse(claim);
    }

    public List<ClaimResponse> getAllClaims() {
        return claimRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ClaimResponse> getClaimsByCustomerId(Long customerId) {
        return claimRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ClaimResponse> getClaimsByPolicyId(Long policyId) {
        return claimRepository.findByPolicyId(policyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ClaimResponse> getClaimsByStatus(ClaimStatus status) {
        return claimRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ClaimResponse updateClaimStatus(Long id, ClaimStatusUpdateRequest request, String authHeader) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with id: " + id));

        CustomerExternalResponse customer = customerServiceClient.getCustomerById(claim.getCustomerId(), authHeader);

        claim.setStatus(request.getStatus());
        Claim updated = claimRepository.save(claim);

        ClaimEvent event = ClaimEvent.builder()
                .claimId(updated.getId())
                .claimNumber(updated.getClaimNumber())
                .customerId(updated.getCustomerId())
                .policyId(updated.getPolicyId())
                .recipientEmail(customer.getEmail())
                .eventType("CLAIM_STATUS_UPDATED")
                .claimStatus(updated.getStatus().name())
                .message("Your claim " + updated.getClaimNumber() + " status has been updated to " + updated.getStatus().name())
                .build();

        claimEventProducer.publishClaimEvent(event);

        return mapToResponse(updated);
    }

    public String deleteClaim(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with id: " + id));

        claimRepository.delete(claim);
        return "Claim deleted successfully";
    }

    private CustomerExternalResponse validateCustomerAndPolicy(ClaimRequest request, String authHeader) {
        CustomerExternalResponse customer = customerServiceClient.getCustomerById(request.getCustomerId(), authHeader);
        if (customer == null || customer.getId() == null) {
            throw new RuntimeException("Customer not found");
        }

        PolicyExternalResponse policy = policyServiceClient.getPolicyById(request.getPolicyId(), authHeader);
        if (policy == null || policy.getId() == null) {
            throw new RuntimeException("Policy not found");
        }

        if (!policy.getCustomerId().equals(request.getCustomerId())) {
            throw new RuntimeException("Policy does not belong to the given customer");
        }

        boolean active = policyServiceClient.isPolicyActive(request.getPolicyId(), authHeader);
        if (!active) {
            throw new RuntimeException("Policy is not active");
        }

        if (request.getClaimAmount().compareTo(policy.getCoverageAmount()) > 0) {
            throw new RuntimeException("Claim amount cannot exceed policy coverage amount");
        }

        return customer;
    }

    private ClaimResponse mapToResponse(Claim claim) {
        return ClaimResponse.builder()
                .id(claim.getId())
                .claimNumber(claim.getClaimNumber())
                .customerId(claim.getCustomerId())
                .policyId(claim.getPolicyId())
                .claimType(claim.getClaimType())
                .claimAmount(claim.getClaimAmount())
                .incidentDate(claim.getIncidentDate())
                .description(claim.getDescription())
                .status(claim.getStatus())
                .createdAt(claim.getCreatedAt())
                .build();
    }
}