package com.cqrs.socialfeed.domain.notification;

import java.util.List;

public interface NotificationRepository {
    Long save(Notification notification);
    List<Notification> findUnreadByUserId(Long userId);
    List<Notification> findRecentUnreadByUserId(Long userId);
    List<Notification> findByUserIdAndCursor(Long userId, Long cursorId, int size);
    int markAsReads(Long userId, List<Long> notificationIds);
    int markAllAsReadByUserId(Long userId);

    void saveAll(List<Notification> notifications);
}
