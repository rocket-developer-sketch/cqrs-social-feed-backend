package com.cqrs.socialfeed.infrakafka.like;

import com.cqrs.socialfeed.domain.like.LikeCountCacheRepository;
import com.cqrs.socialfeed.domain.like.messaging.LikeToggledEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LikeCountConsumer {
    private static final Logger log = LoggerFactory.getLogger(LikeCountConsumer.class);

    private final ObjectMapper objectMapper;
    private final LikeCountCacheRepository likeCountCacheRepository;
    private final KafkaTemplate<String, String> dlqKafkaTemplate;

    public LikeCountConsumer(ObjectMapper objectMapper, LikeCountCacheRepository likeCountCacheRepository,
                             KafkaTemplate<String, String> dlqKafkaTemplate) {
        this.objectMapper = objectMapper;
        this.likeCountCacheRepository = likeCountCacheRepository;
        this.dlqKafkaTemplate = dlqKafkaTemplate;
    }

    @KafkaListener(topics = "like.toggled", groupId = "count-consumer-group")
    public void handleLikeToggled(String message) {
        try {
            LikeToggledEvent event = objectMapper.readValue(message, LikeToggledEvent.class);

            if (event.isLiked()) {
                likeCountCacheRepository.increment(event.getTargetId(), event.getTargetType().name());
                log.info("[CountConsumer] like +1 → targetId: {}, targetType: {}", event.getTargetId(), event.getTargetType());
            } else {
                likeCountCacheRepository.decrement(event.getTargetId(), event.getTargetType().name());
                log.info("[CountConsumer] like -1 → targetId: {}, targetType: {}", event.getTargetId(), event.getTargetType());
            }

        } catch (Exception e) {
            // DLQ로 raw 메시지 전송
            dlqKafkaTemplate.send("like.toggled.dlq", message);

            log.error("[CountConsumer] 처리 실패 → DLQ 전송됨", e);
        }
    }
}
