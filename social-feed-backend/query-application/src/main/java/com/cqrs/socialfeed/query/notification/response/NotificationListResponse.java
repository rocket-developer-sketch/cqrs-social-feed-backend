package com.cqrs.socialfeed.query.notification.response;

import java.util.List;

public class NotificationListResponse {
    private final List<NotificationResponse> notifications;
    private final boolean hasNext;

    public NotificationListResponse(List<NotificationResponse> notifications, boolean hasNext) {
        this.notifications = notifications;
        this.hasNext = hasNext;
    }

    public List<NotificationResponse> getNotifications() {
        return notifications;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
