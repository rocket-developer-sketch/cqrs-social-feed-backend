package com.cqrs.socialfeed.infrawebsocket.notification;

public class ReconnectMessage {
    private final Long userId;

    public ReconnectMessage(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
