package com.cqrs.socialfeed.infrards.notification;

import com.cqrs.socialfeed.domain.notification.NotificationTemplate;
import com.cqrs.socialfeed.domain.notification.NotificationTemplateRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class NotificationTemplateRepositoryImpl implements NotificationTemplateRepository {

    private final NotificationTemplateJpaRepository jpaRepository;

    public NotificationTemplateRepositoryImpl(NotificationTemplateJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<NotificationTemplate> findByCodeAndLocale(String code, String locale) {
        return jpaRepository.findByCodeAndLocale(code, locale)
                .map(e -> new NotificationTemplate(
                        e.getId(),
                        e.getCode(),
                        e.getContent(),
                        e.getLocale(),
                        e.isUseYn()
                ));
    }
}
