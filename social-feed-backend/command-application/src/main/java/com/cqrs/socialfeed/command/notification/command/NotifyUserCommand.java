package com.cqrs.socialfeed.command.notification.command;

import com.cqrs.socialfeed.domain.notification.NotificationType;

import java.util.Map;

public class NotifyUserCommand {
    private final NotificationType type;
    private final Long userId;
    private final Map<String, String> params;
    private final String locale;

    public NotifyUserCommand(NotificationType type, Long userId, Map<String, String> params, String locale) {
        this.type = type;
        this.userId = userId;
        this.params = params;
        this.locale = locale;
    }

    public NotificationType getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getLocale() {
        return locale;
    }
}
