package com.cqrs.socialfeed.infrards.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateJpaRepository extends JpaRepository<NotificationTemplateJpaEntity, Long> {
    Optional<NotificationTemplateJpaEntity> findByCodeAndLocale(String code, String locale);
}
