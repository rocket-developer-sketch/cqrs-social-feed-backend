package com.cqrs.socialfeed.infraredis.notification;

import com.cqrs.socialfeed.domain.notification.NotificationCountCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationCountCacheRepositoryImpl implements NotificationCountCacheRepository {
    private final RedisTemplate<String, Long> redisTemplateLong;

    public NotificationCountCacheRepositoryImpl(RedisTemplate<String, Long> redisTemplateLong) {
        this.redisTemplateLong = redisTemplateLong;
    }

    private String key(Long userId) {
        return "notification:unread:" + userId;
    }

    @Override
    public void increment(Long userId) {
        redisTemplateLong.opsForValue().increment(key(userId));
    }

    @Override
    public void decrement(Long userId, long count) {
        redisTemplateLong.opsForValue().decrement(key(userId), count);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        Long value = redisTemplateLong.opsForValue().get(key(userId));
        return value == null ? 0 : value;
    }
}
