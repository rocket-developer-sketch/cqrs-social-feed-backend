package com.cqrs.socialfeed.domain.notification;

import java.util.Map;

public class NotificationTemplate {
    private final Long id;
    private final String code;
    private final String content;
    private final String locale;
    private final boolean useYn;

    public NotificationTemplate(Long id, String code, String content, String locale, boolean useYn) {
        this.id = id;
        this.code = code;
        this.content = content;
        this.locale = locale;
        this.useYn = useYn;
    }

    public String getCode() {
        return code;
    }

    public String getLocale() {
        return locale;
    }

    public boolean isUsable() {
        return useYn;
    }

    public String render(Map<String, String> params) {
        String result = content;
        for(Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return result;
    }
}
