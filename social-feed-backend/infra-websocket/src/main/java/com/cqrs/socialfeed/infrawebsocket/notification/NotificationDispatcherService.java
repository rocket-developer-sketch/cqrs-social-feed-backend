package com.cqrs.socialfeed.infrawebsocket.notification;

import com.cqrs.socialfeed.domain.notification.Notification;
import com.cqrs.socialfeed.domain.notification.NotificationDispatcher;
import org.springframework.stereotype.Service;

@Service
public class NotificationDispatcherService implements NotificationDispatcher {

    private final NotificationSocketService notificationSocketService;

    public NotificationDispatcherService(NotificationSocketService notificationSocketService) {
        this.notificationSocketService = notificationSocketService;
    }

    @Override
    public void dispatch(Notification notification) {
        notificationSocketService.sendToUser(notification.getUserId(), notification);
    }
}
