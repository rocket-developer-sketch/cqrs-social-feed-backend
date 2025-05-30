package com.cqrs.socialfeed.query.notification.request;

public class GetNotificationRequest {
    private final Long userId;
    private final Long cursorId;
    private final int size;

    public GetNotificationRequest(Long userId, Long cursorId, int size) {
        this.userId = userId;
        this.cursorId = cursorId;
        this.size = size;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCursorId() {
        return cursorId;
    }

    public int getSize() {
        return size;
    }
}
