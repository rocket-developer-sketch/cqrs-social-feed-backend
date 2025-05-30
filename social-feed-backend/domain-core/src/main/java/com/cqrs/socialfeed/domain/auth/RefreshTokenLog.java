package com.cqrs.socialfeed.domain.auth;

import java.time.LocalDateTime;

public class RefreshTokenLog {
    private final Long userId;
    private final LocalDateTime usedAt;
    private final String ipAddress;
    private final String userAgent;
    private final RefreshTokenLogResultType result; // SUCCESS, INVALID_TOKEN, EXPIRED 등

    public RefreshTokenLog(Long userId, LocalDateTime usedAt, String ipAddress, String userAgent, RefreshTokenLogResultType result) {
        this.userId = userId;
        this.usedAt = usedAt;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.result = result;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public RefreshTokenLogResultType getResult() {
        return result;
    }
}
