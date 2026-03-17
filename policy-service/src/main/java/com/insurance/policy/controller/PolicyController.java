package com.insurance.policy.controller;

import com.insurance.policy.dto.PolicyRequest;
import com.insurance.policy.dto.PolicyResponse;
import com.insurance.policy.entity.PolicyStatus;
import com.insurance.policy.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(@Valid @RequestBody PolicyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(policyService.createPolicy(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicyById(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPolicyById(id));
    }

    @GetMapping("/policy-number/{policyNumber}")
    public ResponseEntity<PolicyResponse> getPolicyByPolicyNumber(@PathVariable String policyNumber) {
        return ResponseEntity.ok(policyService.getPolicyByPolicyNumber(policyNumber));
    }

    @GetMapping
    public ResponseEntity<List<PolicyResponse>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PolicyResponse>> getPoliciesByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(policyService.getPoliciesByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PolicyResponse>> getPoliciesByStatus(@PathVariable PolicyStatus status) {
        return ResponseEntity.ok(policyService.getPoliciesByStatus(status));
    }

    @GetMapping("/{id}/active")
    public ResponseEntity<Map<String, Boolean>> isPolicyActive(@PathVariable Long id) {
        return ResponseEntity.ok(Collections.singletonMap("active", policyService.isPolicyActive(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PolicyResponse> updatePolicy(@PathVariable Long id,
                                                       @Valid @RequestBody PolicyRequest request) {
        return ResponseEntity.ok(policyService.updatePolicy(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePolicy(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.deletePolicy(id));
    }
}