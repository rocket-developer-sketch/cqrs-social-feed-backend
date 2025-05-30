package com.cqrs.socialfeed.infraredis.like;

import com.cqrs.socialfeed.domain.like.LikeCountCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LikeCountCacheRepositoryImpl implements LikeCountCacheRepository {

    private final RedisTemplate<String, Long> redisTemplateLong;

    public LikeCountCacheRepositoryImpl(RedisTemplate<String, Long> redisTemplateLong) {
        this.redisTemplateLong = redisTemplateLong;
    }

    private String key(Long id, String type) {
        return type.toLowerCase() + ":" + id + ":likes";  // type을 추가하여 post, comment를 구분
    }

    @Override
    public void increment(Long id, String type) {
        String key = key(id, type);

        if (redisTemplateLong.opsForValue().get(key) == null) {
            redisTemplateLong.opsForValue().set(key, 0L);
        }

        redisTemplateLong.opsForValue().increment(key);
    }

    @Override
    public void decrement(Long id, String type) {
        redisTemplateLong.opsForValue().decrement(key(id, type));
    }

    @Override
    public Long getCount(Long id, String type) {
        Long value = redisTemplateLong.opsForValue().get(key(id, type));
        return value != null ? value : 0L;
    }

    @Override
    public Map<Long, Long> getCounts(List<Long> ids, String type) {
        List<String> keys = ids.stream()
                .map(id -> key(id, type))
                .collect(Collectors.toList());

        List<Long> values = redisTemplateLong.opsForValue().multiGet(keys);

        Map<Long, Long> result = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            Long val = values.get(i);
            Long count = (val != null) ? val : 0L;
            result.put(ids.get(i), count);
        }

        return result;
    }

    @Override
    public void setCount(Long id, Long count, String type) {
        redisTemplateLong.opsForValue().set(key(id, type), count);
    }

}
