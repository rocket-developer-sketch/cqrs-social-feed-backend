package com.cqrs.socialfeed.infrards.like;

import com.cqrs.socialfeed.domain.like.Like;
import com.cqrs.socialfeed.domain.like.LikeCount;
import com.cqrs.socialfeed.domain.like.LikeRepository;
import com.cqrs.socialfeed.domain.like.TargetType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LikeRepositoryImpl implements LikeRepository {
    private final LikeJpaRepository jpaRepository;

    public LikeRepositoryImpl(LikeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Long save(Like like) {
        LikeJpaEntity entity = new LikeJpaEntity(like.getId(), like.getUserId(), like.getTargetId(), like.getTargetType(), like.getCreatedAt());
        return jpaRepository.save(entity).getId();
    }

    @Override
    public boolean existsByUserIdAndTarget(Long userId, Long targetId, TargetType targetType) {
        return jpaRepository.existsByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);
    }

    @Override
    public void deleteByUserIdAndTarget(Long userId, Long targetId, TargetType targetType) {
        jpaRepository.deleteByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);
    }

    @Override
    public long countByTarget(Long targetId, TargetType targetType) {
        return jpaRepository.countByTargetIdAndTargetType(targetId, targetType);
    }

    @Override
    public Map<Long, Long> countByTargetIdsAndType(List<Long> targetIds, TargetType targetType) {
        List<LikeCount> counts =  jpaRepository.countByTargetIdsAndType(targetIds, targetType);
        return counts.stream()
                .collect(Collectors.toMap(
                        LikeCount::getTargetId,
                        LikeCount::getCount
                ));
    }

    @Override
    public Map<Long, Long> getCountsByTargetType(TargetType targetType) {
        List<LikeCount> counts = jpaRepository.getCountsByTargetType(targetType);
        return counts.stream()
                .collect(Collectors.toMap(
                        LikeCount::getTargetId,
                        LikeCount::getCount
                ));
    }
}
