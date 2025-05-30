package com.cqrs.socialfeed.domain.like.messaging;

import com.cqrs.socialfeed.domain.like.TargetType;

public class LikeToggledEvent {
    private TargetType targetType;
    private Long targetId;
    private Long userId;
    private boolean liked; // true: 좋아요, false: 취소
    private String actionAt; // ISO 날짜 (예: "2025-05-22T12:34:56")

    protected LikeToggledEvent() {}


    public LikeToggledEvent(TargetType targetType, Long targetId, Long userId, boolean liked, String actionAt) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.userId = userId;
        this.liked = liked;
        this.actionAt = actionAt;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isLiked() {
        return liked;
    }

    public String getActionAt() {
        return actionAt;
    }
}
