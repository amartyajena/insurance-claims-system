package com.insurance.policy.repository;

import com.insurance.policy.entity.Policy;
import com.insurance.policy.entity.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    boolean existsByPolicyNumber(String policyNumber);
    Optional<Policy> findByPolicyNumber(String policyNumber);
    List<Policy> findByCustomerId(Long customerId);
    List<Policy> findByStatus(PolicyStatus status);
}