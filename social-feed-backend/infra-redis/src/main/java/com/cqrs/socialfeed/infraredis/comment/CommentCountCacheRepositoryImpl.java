package com.cqrs.socialfeed.infraredis.comment;

import com.cqrs.socialfeed.domain.comment.CommentCountCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CommentCountCacheRepositoryImpl  implements CommentCountCacheRepository {

    private final RedisTemplate<String, Long> redisTemplateLong;

    public CommentCountCacheRepositoryImpl(RedisTemplate<String, Long> redisTemplateLong) {
        this.redisTemplateLong = redisTemplateLong;
    }

    private String key(Long postId) {
        return "post:" + postId + ":comments";
    }

    @Override
    public void increment(Long postId) {
        String key = key(postId);

        if (redisTemplateLong.opsForValue().get(key) == null) {
            redisTemplateLong.opsForValue().set(key, 0L);
        }

        redisTemplateLong.opsForValue().increment(key);
    }

    @Override
    public void decrement(Long postId) {
        redisTemplateLong.opsForValue().decrement(key(postId));
    }

    @Override
    public Long getCount(Long postId) {
        Long value = redisTemplateLong.opsForValue().get(key(postId));
        return value != null ? value : 0L;
    }

    @Override
    public Map<Long, Long> getCounts(List<Long> postIds) {
        List<String> keys = postIds.stream()
                .map(this::key)
                .collect(Collectors.toList());

        List<Long> values = redisTemplateLong.opsForValue().multiGet(keys);

        Map<Long, Long> result = new HashMap<>();
        for (int i = 0; i < postIds.size(); i++) {
            Long val = values.get(i);
            Long count = (val != null) ? val : 0L;
            result.put(postIds.get(i), count);
        }

        return result;
    }

    @Override
    public void setCount(Long postId, Long count) {
        redisTemplateLong.opsForValue().set(key(postId), count);
    }
}
