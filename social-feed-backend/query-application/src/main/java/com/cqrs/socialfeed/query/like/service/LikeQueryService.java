package com.cqrs.socialfeed.query.like.service;

import com.cqrs.socialfeed.domain.like.LikeCountCacheRepository;
import com.cqrs.socialfeed.domain.like.LikeRepository;
import com.cqrs.socialfeed.domain.like.TargetType;
import com.cqrs.socialfeed.query.like.usecase.LikeQueryUseCase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LikeQueryService implements LikeQueryUseCase {
    private final LikeCountCacheRepository cacheRepository;
    private final LikeRepository repository; // RDB에서 count(*) 조회

    public LikeQueryService(LikeCountCacheRepository cacheRepository, LikeRepository repository) {
        this.cacheRepository = cacheRepository;
        this.repository = repository;
    }

    @Override
    public Map<Long, Long> getCountsForPostWithFallback(List<Long> postIds, TargetType targetType) {
        Map<Long, Long> cached = cacheRepository.getCounts(postIds, targetType.name());

        // fallback 대상 필터링
        List<Long> missingIds = postIds.stream()
                .filter(id -> cached.get(id) == null)
                .toList();

        Map<Long, Long> fallback = new HashMap<>();
        if (!missingIds.isEmpty()) {
            fallback = repository.countByTargetIdsAndType(missingIds, TargetType.POST);

            // Redis warm-up
            for (Map.Entry<Long, Long> entry : fallback.entrySet()) {
                cacheRepository.setCount(entry.getKey(), entry.getValue(), targetType.name());
            }
        }

        // 최종 병합: Redis에 있던 값 + fallback 값
        Map<Long, Long> result = new HashMap<>(cached);
        result.putAll(fallback);

        return result;
    }
}
