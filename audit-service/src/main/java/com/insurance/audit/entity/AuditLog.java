package com.insurance.audit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long claimId;

    @Column(nullable = false)
    private String claimNumber;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long policyId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String claimStatus;

    @Column(nullable = false, length = 3000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}