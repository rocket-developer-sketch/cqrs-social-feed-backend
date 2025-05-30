package com.cqrs.socialfeed.infrakafka.comment;

import com.cqrs.socialfeed.command.comment.messaging.CommentEventProducer;
import com.cqrs.socialfeed.domain.comment.messaging.CommentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommentEventProducerImpl implements CommentEventProducer {
    private static final Logger log = LoggerFactory.getLogger(CommentEventProducerImpl.class);

    private final KafkaTemplate<String, CommentCreatedEvent> kafkaTemplate;
    private static final String TOPIC = "comment.created";


    public CommentEventProducerImpl(KafkaTemplate<String, CommentCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(CommentCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getPostId().toString(), event);
        log.info("[Kafka] Sent CommentCreatedEvent to {}: {}", TOPIC, event);
    }
}
