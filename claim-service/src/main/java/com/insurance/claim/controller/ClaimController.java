package com.insurance.claim.controller;

import com.insurance.claim.dto.ClaimRequest;
import com.insurance.claim.dto.ClaimResponse;
import com.insurance.claim.dto.ClaimStatusUpdateRequest;
import com.insurance.claim.entity.ClaimStatus;
import com.insurance.claim.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    public ResponseEntity<ClaimResponse> createClaim(@Valid @RequestBody ClaimRequest request,
                                                     @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED).body(claimService.createClaim(request, authHeader));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getClaimById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    @GetMapping("/claim-number/{claimNumber}")
    public ResponseEntity<ClaimResponse> getClaimByClaimNumber(@PathVariable String claimNumber) {
        return ResponseEntity.ok(claimService.getClaimByClaimNumber(claimNumber));
    }

    @GetMapping
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(claimService.getClaimsByCustomerId(customerId));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByPolicyId(@PathVariable Long policyId) {
        return ResponseEntity.ok(claimService.getClaimsByPolicyId(policyId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByStatus(@PathVariable ClaimStatus status) {
        return ResponseEntity.ok(claimService.getClaimsByStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.deleteClaim(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClaimResponse> updateClaimStatus(@PathVariable Long id,
                                                           @Valid @RequestBody ClaimStatusUpdateRequest request,
                                                           @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(claimService.updateClaimStatus(id, request, authHeader));
    }
}