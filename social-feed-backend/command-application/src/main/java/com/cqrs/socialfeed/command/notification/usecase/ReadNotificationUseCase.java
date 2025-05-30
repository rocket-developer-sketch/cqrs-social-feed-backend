package com.cqrs.socialfeed.command.notification.usecase;

import com.cqrs.socialfeed.command.notification.command.MarkAllNotificationsAsReadCommand;
import com.cqrs.socialfeed.command.notification.command.MarkNotificationAsReadCommand;

public interface ReadNotificationUseCase {
    void markMultipleAsRead(MarkNotificationAsReadCommand command);
    void markAllAsRead(MarkAllNotificationsAsReadCommand command);
}
