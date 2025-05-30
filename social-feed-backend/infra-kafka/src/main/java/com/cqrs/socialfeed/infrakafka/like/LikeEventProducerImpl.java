package com.cqrs.socialfeed.infrakafka.like;

import com.cqrs.socialfeed.command.like.messaging.LikeEventProducer;
import com.cqrs.socialfeed.domain.like.messaging.LikeToggledEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LikeEventProducerImpl implements LikeEventProducer {
    private static final Logger log = LoggerFactory.getLogger(LikeEventProducerImpl.class);

    private final KafkaTemplate<String, LikeToggledEvent> kafkaTemplate;
    private static final String TOPIC = "like.toggled";

    public LikeEventProducerImpl(KafkaTemplate<String, LikeToggledEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(LikeToggledEvent event) {
        kafkaTemplate.send(TOPIC, event.getTargetId().toString(), event);
        log.info("[Kafka] Sent LikeToggledEvent to {}: {}", TOPIC, event);
    }
}
