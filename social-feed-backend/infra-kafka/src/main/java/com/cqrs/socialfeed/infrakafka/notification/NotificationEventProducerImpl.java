package com.cqrs.socialfeed.infrakafka.notification;

import com.cqrs.socialfeed.command.notification.messaging.NotificationEventProducer;
import com.cqrs.socialfeed.domain.notification.messaging.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventProducerImpl implements NotificationEventProducer {
    private static final Logger log = LoggerFactory.getLogger(NotificationEventProducerImpl.class);

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private static final String TOPIC = "notification.created";

    public NotificationEventProducerImpl(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(NotificationEvent event) {
        //kafkaTemplate.send(TOPIC, event.getReceiverId().toString(), event);
        kafkaTemplate.send(TOPIC, event);
        log.info("[Kafka] Sent NotificationEvent to {}: {}", TOPIC, event);
    }
}
