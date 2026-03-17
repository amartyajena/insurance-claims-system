package com.insurance.claim.repository;

import com.insurance.claim.entity.Claim;
import com.insurance.claim.entity.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    boolean existsByClaimNumber(String claimNumber);
    Optional<Claim> findByClaimNumber(String claimNumber);
    List<Claim> findByCustomerId(Long customerId);
    List<Claim> findByPolicyId(Long policyId);
    List<Claim> findByStatus(ClaimStatus status);
}