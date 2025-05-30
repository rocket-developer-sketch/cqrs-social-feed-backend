package com.cqrs.socialfeed.command.notification.command;

import java.util.List;

public class MarkNotificationAsReadCommand {
    private final Long userId;
    private final List<Long> notificationIds;

    public MarkNotificationAsReadCommand(Long userId, List<Long> notificationIds) {
        this.userId = userId;
        this.notificationIds = notificationIds;
    }

    public Long getUserId() {
        return userId;
    }

    public List<Long> getNotificationIds() {
        return notificationIds;
    }
}
