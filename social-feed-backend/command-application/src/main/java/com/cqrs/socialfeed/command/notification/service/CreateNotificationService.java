package com.cqrs.socialfeed.command.notification.service;

import com.cqrs.socialfeed.command.notification.command.MarkAllNotificationsAsReadCommand;
import com.cqrs.socialfeed.command.notification.command.MarkNotificationAsReadCommand;
import com.cqrs.socialfeed.command.notification.command.NotifyUserCommand;
import com.cqrs.socialfeed.command.notification.usecase.ReadNotificationUseCase;
import com.cqrs.socialfeed.domain.notification.*;
import com.cqrs.socialfeed.command.notification.usecase.NotifyUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
public class CreateNotificationService implements NotifyUseCase, ReadNotificationUseCase {
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationCountCacheRepository notificationCountCacheRepository;

    public CreateNotificationService(NotificationTemplateRepository notificationTemplateRepository,
                                     NotificationRepository notificationRepository, NotificationCountCacheRepository notificationCountCacheRepository) {
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.notificationRepository = notificationRepository;
        this.notificationCountCacheRepository = notificationCountCacheRepository;
    }

    @Override
    public void notify(NotifyUserCommand command) {
        NotificationTemplate template = notificationTemplateRepository
                .findByCodeAndLocale(command.getType().getTemplateCode(), command.getLocale())
                .orElseThrow(() -> new RuntimeException("템플릿 없음"));

        String message = template.render(command.getParams());

        Notification notification = new Notification(
                null,
                command.getUserId(),
                message,
                command.getType(),
                false,
                LocalDateTime.now()
        );

        notificationRepository.save(notification);
    }

    @Override
    public void markMultipleAsRead(MarkNotificationAsReadCommand command) {
        int updatedCount = notificationRepository.markAsReads(command.getUserId(), command.getNotificationIds());
        if (updatedCount > 0) {
            // 읽은 알림 수만큼 카운트 감소
            notificationCountCacheRepository.decrement(command.getUserId(), command.getNotificationIds().size());
        }
    }

    @Override
    public void markAllAsRead(MarkAllNotificationsAsReadCommand command) {
        int updatedCount = notificationRepository.markAllAsReadByUserId(command.getUserId());
        if (updatedCount > 0) {
            notificationCountCacheRepository.decrement(command.getUserId(), updatedCount);
        }
    }
}
