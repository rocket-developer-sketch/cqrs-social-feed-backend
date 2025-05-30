package com.cqrs.socialfeed.command.notification.messaging;

import com.cqrs.socialfeed.domain.notification.messaging.NotificationEvent;

public interface NotificationEventProducer {
    void send(NotificationEvent event);
}
