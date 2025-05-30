package com.cqrs.socialfeed.infraredis.comment;

import com.cqrs.socialfeed.domain.comment.CommentCountWarmUpCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommentCountWarmUpCacheRepositoryImpl implements CommentCountWarmUpCacheRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public CommentCountWarmUpCacheRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setCacheWarmUpKey() {
        redisTemplate.opsForValue().set("cache:comment_count:preloaded", "true");
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

}
