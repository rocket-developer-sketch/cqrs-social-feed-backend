package com.cqrs.socialfeed.domain.auth;

import java.time.LocalDateTime;

public class RefreshToken {
    private final Long userId;
    private final String token;
    private final LocalDateTime expiresAt;

    public RefreshToken(Long userId, String token, LocalDateTime expiresAt) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
