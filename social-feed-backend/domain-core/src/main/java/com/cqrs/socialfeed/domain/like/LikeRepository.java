package com.cqrs.socialfeed.domain.like;


import java.util.List;
import java.util.Map;

public interface LikeRepository {
    Long save(Like like);

    boolean existsByUserIdAndTarget(Long userId, Long targetId, TargetType targetType);

    void deleteByUserIdAndTarget(Long userId, Long targetId, TargetType targetType);

    long countByTarget(Long targetId, TargetType targetType);

    Map<Long, Long> countByTargetIdsAndType(List<Long> targetIds, TargetType targetType);

    Map<Long, Long> getCountsByTargetType(TargetType targetType);
}
