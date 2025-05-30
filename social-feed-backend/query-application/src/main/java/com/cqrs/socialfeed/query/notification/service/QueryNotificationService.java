package com.cqrs.socialfeed.query.notification.service;

import com.cqrs.socialfeed.domain.notification.Notification;
import com.cqrs.socialfeed.domain.notification.NotificationCountCacheRepository;
import com.cqrs.socialfeed.domain.notification.NotificationRepository;
import com.cqrs.socialfeed.query.notification.request.GetNotificationRequest;
import com.cqrs.socialfeed.query.notification.response.NotificationListResponse;
import com.cqrs.socialfeed.query.notification.response.NotificationResponse;
import com.cqrs.socialfeed.query.notification.usecase.NotificationQueryUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryNotificationService implements NotificationQueryUseCase {
    private final NotificationRepository notificationRepository;
    private final NotificationCountCacheRepository notificationCountCacheRepository;

    public QueryNotificationService(NotificationRepository notificationRepository, NotificationCountCacheRepository notificationCountCacheRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationCountCacheRepository = notificationCountCacheRepository;
    }

    public NotificationListResponse getNotifications(GetNotificationRequest request) {
        List<Notification> notifications = notificationRepository
                .findByUserIdAndCursor(request.getUserId(), request.getCursorId(), request.getSize() + 1);

        boolean hasNext = notifications.size() > request.getSize();
        if (hasNext) {
            notifications.remove(notifications.size() - 1);
        }

        List<NotificationResponse> responseList = notifications.stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());

        return new NotificationListResponse(responseList, hasNext);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return notificationCountCacheRepository.getUnreadCount(userId);
    }
}
