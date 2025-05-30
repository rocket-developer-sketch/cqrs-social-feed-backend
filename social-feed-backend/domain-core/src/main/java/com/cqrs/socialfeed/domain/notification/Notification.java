package com.cqrs.socialfeed.domain.notification;

import java.time.LocalDateTime;

public class Notification {
    private final Long id;
    private final Long userId;
    private final String content;
    private final NotificationType notificationType;
    private final boolean read;
    private final LocalDateTime createdAt;

    public Notification(Long id, Long userId, String content, NotificationType notificationType, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.notificationType = notificationType;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", notificationType=" + notificationType +
                ", read=" + read +
                ", createdAt=" + createdAt +
                '}';
    }

    //    private final Long id;
//    private final Long userId; // 알림 받는 유저
//    private final String type; // (enum 또는 문자열: "like", "comment", "follow", "post")
//    private final Long sourceUserId; // 알림 발생시킨 유저
//    private final Long postId; // nullable
//    private final Boolean isRead;
//    private final LocalDateTime createdAt;
//
//    public Notification(Long id, Long userId, String type, Long sourceUserId, Long postId, Boolean isRead, LocalDateTime createdAt) {
//        this.id = id;
//        this.userId = userId;
//        this.type = type;
//        this.sourceUserId = sourceUserId;
//        this.postId = postId;
//        this.isRead = isRead;
//        this.createdAt = createdAt;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public Long getSourceUserId() {
//        return sourceUserId;
//    }
//
//    public Long getPostId() {
//        return postId;
//    }
//
//    public Boolean getRead() {
//        return isRead;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
}
