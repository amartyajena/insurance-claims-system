package com.insurance.audit.repository;

import com.insurance.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByClaimId(Long claimId);
    List<AuditLog> findByEventType(String eventType);
    List<AuditLog> findByClaimStatus(String claimStatus);
}