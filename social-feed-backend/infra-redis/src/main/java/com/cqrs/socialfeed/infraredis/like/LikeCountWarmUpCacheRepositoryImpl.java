package com.cqrs.socialfeed.infraredis.like;

import com.cqrs.socialfeed.domain.like.LikeCountWarmUpCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LikeCountWarmUpCacheRepositoryImpl implements LikeCountWarmUpCacheRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public LikeCountWarmUpCacheRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void setCacheWarmUpKey() {
        redisTemplate.opsForValue().set("cache:like_count:preloaded", "true");
    }
}
