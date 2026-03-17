package com.insurance.policy.service;

import com.insurance.policy.dto.PolicyRequest;
import com.insurance.policy.dto.PolicyResponse;
import com.insurance.policy.entity.Policy;
import com.insurance.policy.entity.PolicyStatus;
import com.insurance.policy.repository.PolicyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public PolicyResponse createPolicy(PolicyRequest request) {
        validateDates(request);

        if (policyRepository.existsByPolicyNumber(request.getPolicyNumber())) {
            throw new RuntimeException("Policy number already exists");
        }

        Policy policy = Policy.builder()
                .policyNumber(request.getPolicyNumber())
                .customerId(request.getCustomerId())
                .policyType(request.getPolicyType())
                .coverageAmount(request.getCoverageAmount())
                .premiumAmount(request.getPremiumAmount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .build();

        Policy saved = policyRepository.save(policy);
        return mapToResponse(saved);
    }

    public PolicyResponse getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));
        return mapToResponse(policy);
    }

    public PolicyResponse getPolicyByPolicyNumber(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new RuntimeException("Policy not found with policy number: " + policyNumber));
        return mapToResponse(policy);
    }

    public List<PolicyResponse> getAllPolicies() {
        return policyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<PolicyResponse> getPoliciesByCustomerId(Long customerId) {
        return policyRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<PolicyResponse> getPoliciesByStatus(PolicyStatus status) {
        return policyRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PolicyResponse updatePolicy(Long id, PolicyRequest request) {
        validateDates(request);

        Policy existing = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));

        if (!existing.getPolicyNumber().equals(request.getPolicyNumber())
                && policyRepository.existsByPolicyNumber(request.getPolicyNumber())) {
            throw new RuntimeException("Policy number already exists");
        }

        existing.setPolicyNumber(request.getPolicyNumber());
        existing.setCustomerId(request.getCustomerId());
        existing.setPolicyType(request.getPolicyType());
        existing.setCoverageAmount(request.getCoverageAmount());
        existing.setPremiumAmount(request.getPremiumAmount());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setStatus(request.getStatus());

        Policy updated = policyRepository.save(existing);
        return mapToResponse(updated);
    }

    public String deletePolicy(Long id) {
        Policy existing = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));

        policyRepository.delete(existing);
        return "Policy deleted successfully";
    }

    public boolean isPolicyActive(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));

        return calculateActive(policy);
    }

    private void validateDates(PolicyRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }
    }

    private boolean calculateActive(Policy policy) {
        LocalDate today = LocalDate.now();
        return policy.getStatus() == PolicyStatus.ACTIVE
                && !today.isBefore(policy.getStartDate())
                && !today.isAfter(policy.getEndDate());
    }

    private PolicyResponse mapToResponse(Policy policy) {
        return PolicyResponse.builder()
                .id(policy.getId())
                .policyNumber(policy.getPolicyNumber())
                .customerId(policy.getCustomerId())
                .policyType(policy.getPolicyType())
                .coverageAmount(policy.getCoverageAmount())
                .premiumAmount(policy.getPremiumAmount())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .status(policy.getStatus())
                .active(calculateActive(policy))
                .build();
    }
}