package com.cqrs.socialfeed.domain.notification;

public interface NotificationCountCacheRepository {
    void increment(Long userId);
    void decrement(Long userId, long count);
    Long getUnreadCount(Long userId);
}
