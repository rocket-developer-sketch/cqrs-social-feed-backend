package com.cqrs.socialfeed.query.notification.response;

import com.cqrs.socialfeed.domain.notification.Notification;

import java.time.LocalDateTime;

public class NotificationResponse {
    private final Long id;
    private final String type;
    private final String content;
    private final boolean isRead;
    private final LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getNotificationType().name(),
                notification.getContent(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    public NotificationResponse(Long id, String type, String content, boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
