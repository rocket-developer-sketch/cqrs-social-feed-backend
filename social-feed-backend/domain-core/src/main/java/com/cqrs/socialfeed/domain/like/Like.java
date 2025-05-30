package com.cqrs.socialfeed.domain.like;

import java.time.LocalDateTime;

public class Like {
    private final Long id;
    private final Long userId;
    private final Long targetId;
    private final TargetType targetType;
    private final LocalDateTime createdAt;

    public Like(Long id, Long userId, Long targetId, TargetType targetType, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
