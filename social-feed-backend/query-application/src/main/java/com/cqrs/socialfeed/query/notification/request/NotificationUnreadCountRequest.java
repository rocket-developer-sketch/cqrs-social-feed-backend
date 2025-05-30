package com.cqrs.socialfeed.query.notification.request;

public class NotificationUnreadCountRequest {
    private final Long userId;

    public NotificationUnreadCountRequest(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
