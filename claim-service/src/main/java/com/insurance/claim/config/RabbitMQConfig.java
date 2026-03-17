package com.insurance.claim.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLAIM_EXCHANGE = "claim.exchange";
    public static final String CLAIM_NOTIFICATION_QUEUE = "claim.notification.queue";
    public static final String CLAIM_AUDIT_QUEUE = "claim.audit.queue";

    public static final String CLAIM_NOTIFICATION_ROUTING_KEY = "claim.notification";
    public static final String CLAIM_AUDIT_ROUTING_KEY = "claim.audit";

    @Bean
    public TopicExchange claimExchange() {
        return new TopicExchange(CLAIM_EXCHANGE);
    }

    @Bean
    public Queue claimNotificationQueue() {
        return new Queue(CLAIM_NOTIFICATION_QUEUE);
    }

    @Bean
    public Queue claimAuditQueue() {
        return new Queue(CLAIM_AUDIT_QUEUE);
    }

    @Bean
    public Binding claimNotificationBinding() {
        return BindingBuilder
                .bind(claimNotificationQueue())
                .to(claimExchange())
                .with(CLAIM_NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Binding claimAuditBinding() {
        return BindingBuilder
                .bind(claimAuditQueue())
                .to(claimExchange())
                .with(CLAIM_AUDIT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}