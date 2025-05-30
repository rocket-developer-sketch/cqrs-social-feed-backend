package com.cqrs.socialfeed.query.notification.usecase;

import com.cqrs.socialfeed.query.notification.request.GetNotificationRequest;
import com.cqrs.socialfeed.query.notification.response.NotificationListResponse;

public interface NotificationQueryUseCase {
    Long getUnreadCount(Long userId);
    NotificationListResponse getNotifications(GetNotificationRequest request);
}
