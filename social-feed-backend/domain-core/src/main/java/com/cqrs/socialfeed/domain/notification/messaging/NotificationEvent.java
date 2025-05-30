package com.cqrs.socialfeed.domain.notification.messaging;

import com.cqrs.socialfeed.domain.notification.NotificationType;

public class NotificationEvent {
    private NotificationType type;    // ex: COMMENT_NEW, LIKE_NEW
    //private List<Long> receiverIds;          // 알림 대상 사용자
    private Long receiverId;
    private Long senderId;            // 알림 생성자 (optional)
    private String message;           // 알림 메시지
    private Long relatedTargetId;       // 관련 컨텐츠 ID (optional)
    private String createdAt;         // ISO 포맷

    protected NotificationEvent() {}

    public NotificationEvent(NotificationType type, Long receiverId, Long senderId, String message, Long relatedTargetId, String createdAt) {
        this.type = type;
        //this.receiverIds = receiverIds;
        this.senderId = senderId;
        this.message = message;
        this.relatedTargetId = relatedTargetId;
        this.createdAt = createdAt;
    }

    public NotificationType getType() { return type; }
    //public List<Long> getReceiverIds() { return receiverIds; }
    public Long getReceiverId() {
        return receiverId;
    }
    public Long getSenderId() { return senderId; }
    public String getMessage() { return message; }
    public Long getRelatedTargetId() { return relatedTargetId; }
    public String getCreatedAt() { return createdAt; }

}
