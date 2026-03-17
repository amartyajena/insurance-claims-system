package com.insurance.audit.consumer;

import com.insurance.audit.config.RabbitMQConfig;
import com.insurance.audit.event.ClaimEvent;
import com.insurance.audit.service.AuditService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClaimAuditConsumer {

    private final AuditService auditService;

    public ClaimAuditConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @RabbitListener(queues = RabbitMQConfig.CLAIM_AUDIT_QUEUE)
    public void consumeClaimEvent(ClaimEvent event) {
        auditService.saveAuditLog(event);
    }
}