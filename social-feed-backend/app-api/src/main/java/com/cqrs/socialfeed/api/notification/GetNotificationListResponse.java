package com.cqrs.socialfeed.api.notification;

import com.cqrs.socialfeed.query.notification.response.NotificationResponse;

import java.util.List;

public record GetNotificationListResponse(
        List<NotificationResponse> notifications,
        boolean hasNext
) {}
