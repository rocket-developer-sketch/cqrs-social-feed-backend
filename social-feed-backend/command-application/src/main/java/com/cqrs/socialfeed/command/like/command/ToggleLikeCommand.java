package com.cqrs.socialfeed.command.like.command;

import com.cqrs.socialfeed.domain.like.TargetType;

public class ToggleLikeCommand {
    private final Long userId;
    private final Long targetId;
    private final TargetType targetType;
    private final String locale;

    public ToggleLikeCommand(Long userId, Long targetId, TargetType targetType, String locale) {
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.locale = locale;
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

    public String getLocale() {
        return locale;
    }
}
