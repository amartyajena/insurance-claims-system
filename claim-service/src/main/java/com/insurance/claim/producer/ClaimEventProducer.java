package com.insurance.claim.producer;

import com.insurance.claim.config.RabbitMQConfig;
import com.insurance.claim.event.ClaimEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClaimEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public ClaimEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishClaimEvent(ClaimEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CLAIM_EXCHANGE,
                RabbitMQConfig.CLAIM_NOTIFICATION_ROUTING_KEY,
                event
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CLAIM_EXCHANGE,
                RabbitMQConfig.CLAIM_AUDIT_ROUTING_KEY,
                event
        );
    }
}