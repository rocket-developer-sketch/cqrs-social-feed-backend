package com.cqrs.socialfeed.infrakafka.notification;

import com.cqrs.socialfeed.domain.notification.Notification;
import com.cqrs.socialfeed.domain.notification.NotificationDispatcher;
import com.cqrs.socialfeed.domain.notification.NotificationRepository;
import com.cqrs.socialfeed.domain.notification.messaging.NotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationDlqListener {
    private static final Logger log = LoggerFactory.getLogger(NotificationDlqListener.class);

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationDispatcher notificationDispatcher;

    private static final int MAX_RETRIES = 3;  // 최대 재시도 횟수
    private Map<String, Integer> failureCountMap = new HashMap<>();

    public NotificationDlqListener(ObjectMapper objectMapper, NotificationRepository notificationRepository, NotificationDispatcher notificationDispatcher) {
        this.objectMapper = objectMapper;
        this.notificationRepository = notificationRepository;
        this.notificationDispatcher = notificationDispatcher;
    }

    @KafkaListener(topics = "notification.created.dlq", groupId = "notification-dlq-monitor")
    public void logFailedMessage(String raw) {
        try {
            // 실패 메시지 로깅
            log.warn("[DLQ 수신] Kafka 메시지 처리 실패 → 수동 확인 필요: {}", raw);

            // DLQ에서 받은 메시지를 NotificationEvent 객체로 역직렬화
            NotificationEvent event = objectMapper.readValue(raw, NotificationEvent.class);

            // 실패 횟수 추적
            int failureCount = getFailureCount(raw);  // 실패 횟수를 메모리에서 가져오기

            if (failureCount >= MAX_RETRIES) {
                // 실패 횟수가 최대 재시도 횟수를 초과한 경우 처리
                sendFailureAlert(raw);  // Slack 알림 예시
                log.error("[DLQ] 최대 재시도 횟수 초과 → 메시지 수동 처리 필요: {}", raw);
            } else {
                // 재시도 처리: 메시지 다시 처리
                retryProcessingMessage(event, failureCount);
            }
        } catch (Exception e) {
            log.error("[DLQ 수신 실패] DLQ 메시지 처리 중 오류 발생: {}", raw, e);
        }
    }

    // 실패 메시지를 다시 처리하는 로직
    private void retryProcessingMessage(NotificationEvent event, int failureCount) {
        try {
            // 실패 횟수 증가
            failureCount++;

            List<Notification> notifications = new ArrayList<>();

            for (Long receiverId : event.getReceiverIds()) {
                // Notification 객체 생성
                Notification notification = new Notification(
                        null,
                        receiverId,  // 각 receiverId에 대해 개별적인 알림을 생성
                        event.getMessage(),
                        event.getType(),
                        false,
                        LocalDateTime.parse(event.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                );
                notifications.add(notification);  // 알림 리스트에 추가
            }

            // DB에 알림 저장 (배치 저장)
            notificationRepository.saveAll(notifications);  // 여러 알림을 한 번에 저장

            // 각 receiverId에 대해 WebSocket 실시간 전송
            for (Notification notification : notifications) {
                notificationDispatcher.dispatch(notification);  // WebSocket 전송
            }

            // 실패 횟수 업데이트 (메모리 기반)
            updateFailureCount(event, failureCount);

            log.info("[DLQ 재시도 성공] 메시지 재처리 완료: {}", event);

        } catch (Exception e) {
            // 재시도 실패 시 DLQ로 다시 보내거나 추가적인 처리가 필요
            log.error("[DLQ 재시도 실패] 메시지 처리 중 오류 발생: {}", event, e);
            // 실패한 메시지를 다시 DLQ로 전송하여 추후 처리를 할 수 있음
            // dlqKafkaTemplate.send("notification.created.dlq", event);
        }
    }

    // 실패 횟수를 가져오기
    private int getFailureCount(String message) {
        return failureCountMap.getOrDefault(message, 0);  // 기본값은 0
    }

    // 실패 횟수를 업데이트
    private void updateFailureCount(NotificationEvent event, int failureCount) {
        failureCountMap.put(event.getMessage(), failureCount);
    }

    // 실패 메시지 알림 (Slack, Email 등)
    private void sendFailureAlert(String message) {
        // 실패한 메시지에 대해 알림을 보내는 로직
        // 예시: Slack API를 사용하여 알림을 보내는 방식 (주석 처리됨)
        // slackService.sendMessage("실패한 메시지 처리 필요: " + message);
        log.warn("[ALERT] Kafka 메시지 처리 실패, 재시도 횟수 초과: {}", message);
    }
}
