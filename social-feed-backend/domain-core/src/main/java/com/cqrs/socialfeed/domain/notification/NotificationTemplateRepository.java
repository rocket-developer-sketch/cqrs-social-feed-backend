package com.cqrs.socialfeed.domain.notification;

import java.util.Optional;

public interface NotificationTemplateRepository {
    Optional<NotificationTemplate> findByCodeAndLocale(String code, String locale);
}
