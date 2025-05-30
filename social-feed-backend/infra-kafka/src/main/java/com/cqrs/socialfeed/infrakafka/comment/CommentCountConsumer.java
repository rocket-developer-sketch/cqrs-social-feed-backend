package com.cqrs.socialfeed.infrakafka.comment;

import com.cqrs.socialfeed.domain.comment.CommentCountCacheRepository;
import com.cqrs.socialfeed.domain.comment.messaging.CommentCreatedEvent;
import com.cqrs.socialfeed.infrakafka.notification.NotificationDlqListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CommentCountConsumer {
    private static final Logger log = LoggerFactory.getLogger(CommentCountConsumer.class);

    private final ObjectMapper objectMapper;
    private final CommentCountCacheRepository commentCountCacheRepository;

    public CommentCountConsumer(ObjectMapper objectMapper, CommentCountCacheRepository commentCountCacheRepository) {
        this.objectMapper = objectMapper;
        this.commentCountCacheRepository = commentCountCacheRepository;
    }

    @KafkaListener(topics = "comment.created", groupId = "count-consumer-group")
    public void handleCommentCreated(String message) {
        try {
            CommentCreatedEvent event = objectMapper.readValue(message, CommentCreatedEvent.class);
            commentCountCacheRepository.increment(event.getPostId());
            log.info("[CountConsumer] comment +1 → postId: {}", event.getPostId());
        } catch (Exception e) {
            log.error("[CountConsumer] Failed to process comment.created message", e);
        }
    }

    /*
    * TODO 구현하기
    * */
//    @KafkaListener(topics = "comment.deleted", groupId = "count-consumer-group")
//    public void handleCommentDeleted(String message) {
//        try {
//            CommentDeletedEvent event = objectMapper.readValue(message, CommentDeletedEvent.class);
//            commentCountCacheRepository.decrement(event.getPostId());
//            log.info("[CountConsumer] comment -1 → postId: {}", event.getPostId());
//        } catch (Exception e) {
//            log.error("[CountConsumer] Failed to process comment.deleted message", e);
//        }
//    }

}
