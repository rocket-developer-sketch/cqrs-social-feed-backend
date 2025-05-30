package com.cqrs.socialfeed.infrakafka.post;


import com.cqrs.socialfeed.command.post.messaging.PostEventProducer;
import com.cqrs.socialfeed.domain.post.messaging.PostCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class PostEventProducerImpl implements PostEventProducer {
    private static final Logger log = LoggerFactory.getLogger(PostEventProducerImpl.class);

    private final KafkaTemplate<String, PostCreatedEvent> kafkaTemplate;
    private final String TOPIC = "post.created";

    public PostEventProducerImpl(KafkaTemplate<String, PostCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public void send(PostCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getPostId().toString(), event);
        log.info("[Kafka] Sent PostCreatedEvent to {}: {}", TOPIC, event);
    }
}