package com.cqrs.socialfeed.infrawebsocket.notification;

import com.cqrs.socialfeed.domain.notification.Notification;
import com.cqrs.socialfeed.domain.notification.NotificationDispatcher;
import com.cqrs.socialfeed.domain.notification.NotificationRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Component
public class NotificationWebSocketHandler {

    private final NotificationRepository notificationRepository;
    private final NotificationDispatcher notificationDispatcher;

    public NotificationWebSocketHandler(NotificationRepository notificationRepository, NotificationDispatcher notificationDispatcher) {
        this.notificationRepository = notificationRepository;
        this.notificationDispatcher = notificationDispatcher;
    }

    /*
    * TODO 테스트 필요
    * */
    // request: /app/notifications/reconnect
    @MessageMapping("/notifications/reconnect")
    public void handleReconnect(ReconnectMessage message, Principal principal) {
        Long userId = message.getUserId(); // 또는 principal.getName() → Long.parseLong()

        List<Notification> unread = notificationRepository.findRecentUnreadByUserId(userId);

        unread.forEach(notificationDispatcher::dispatch);
    }
}
