package com.cqrs.socialfeed.infrawebsocket.notification;

import com.cqrs.socialfeed.domain.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationSocketService {
    private final Logger log = LoggerFactory.getLogger(NotificationSocketService.class);
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToUser(Long userId, Notification notification) {
        String destination = "/topic/notification/" + userId;
        messagingTemplate.convertAndSend(destination, notification);
        log.info("[WebSocket] 알림 전송 완료 → {}, notification: {}", destination, notification);
    }
}
