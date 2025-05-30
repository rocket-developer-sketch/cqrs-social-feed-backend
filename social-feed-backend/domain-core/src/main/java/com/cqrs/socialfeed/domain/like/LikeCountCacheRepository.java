package com.cqrs.socialfeed.domain.like;

import java.util.List;
import java.util.Map;

public interface LikeCountCacheRepository {
    void increment(Long targetId, String targetType);
    void decrement(Long targetId, String targetType);
    Long getCount(Long targetId, String targetType);
    Map<Long, Long> getCounts(List<Long> targetIds, String targetType);
    void setCount(Long targetId, Long count, String targetType);
}
