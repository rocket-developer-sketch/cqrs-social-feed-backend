package com.cqrs.socialfeed.infrakafka.notification;

import com.cqrs.socialfeed.domain.notification.Notification;
import com.cqrs.socialfeed.domain.notification.NotificationCountCacheRepository;
import com.cqrs.socialfeed.domain.notification.NotificationDispatcher;
import com.cqrs.socialfeed.domain.notification.NotificationRepository;
import com.cqrs.socialfeed.domain.notification.messaging.NotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NotificationEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationCountCacheRepository notificationCountCacheRepository;
    private final NotificationDispatcher notificationDispatcher;
    private final KafkaTemplate<String, String> dlqKafkaTemplate;


    public NotificationEventConsumer(ObjectMapper objectMapper, NotificationRepository notificationRepository, NotificationCountCacheRepository notificationCountCacheRepository, NotificationDispatcher notificationDispatcher, KafkaTemplate<String, String> dlqKafkaTemplate) {
        this.objectMapper = objectMapper;
        this.notificationRepository = notificationRepository;
        this.notificationCountCacheRepository = notificationCountCacheRepository;
        this.notificationDispatcher = notificationDispatcher;
        this.dlqKafkaTemplate = dlqKafkaTemplate;
    }

    @KafkaListener(topics = "notification.created", groupId = "notification-consumer-group")
    public void consume(String message) {
        try {
            // 메시지를 NotificationEvent 객체로 변환
            NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);
            
            // Notification 객체 생성
            Notification notification = new Notification(
                    event.getRelatedTargetId(),
                    event.getReceiverId(),
                    event.getMessage(),
                    event.getType(),
                    false,
                    LocalDateTime.parse(event.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );

            notificationRepository.save(notification); // rdbs 저장
            notificationCountCacheRepository.increment(notification.getUserId()); // cache db count 저장
            notificationDispatcher.dispatch(notification); // websocket 사용자 수신 알림

            log.info("[NotificationConsumer] 알림 처리 완료 → userId: {}", event.getReceiverId());


        } catch (Exception e) {
            dlqKafkaTemplate.send("notification.created.dlq", message);

            log.error("[NotificationConsumer] 처리 실패 → DLQ 전송됨", e);

        }
    }
}
