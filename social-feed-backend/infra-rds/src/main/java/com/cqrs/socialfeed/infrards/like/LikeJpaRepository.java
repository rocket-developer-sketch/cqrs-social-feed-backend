package com.cqrs.socialfeed.infrards.like;

import com.cqrs.socialfeed.domain.like.LikeCount;
import com.cqrs.socialfeed.domain.like.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeJpaRepository extends JpaRepository<LikeJpaEntity, Long> {
    boolean existsByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, TargetType targetType);
    void deleteByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, TargetType targetType);
    long countByTargetIdAndTargetType(Long targetId, TargetType targetType);


    @Query("""
        SELECT new com.cqrs.socialfeed.domain.like.LikeCount(
            l.targetId,
            COUNT(l)
        )
        FROM LikeJpaEntity l
        WHERE l.targetType = :targetType
          AND l.targetId IN :targetIds
        GROUP BY l.targetId
    """)
    List<LikeCount> countByTargetIdsAndType(
            @Param("targetIds") List<Long> targetIds,
            @Param("targetType") TargetType targetType
    );

    @Query("""
        SELECT new com.cqrs.socialfeed.domain.like.LikeCount(
            l.targetId,
            COUNT(l)
        )
        FROM LikeJpaEntity l
        WHERE l.targetType = :targetType
        GROUP BY l.targetId
    """)
    List<LikeCount> getCountsByTargetType(@Param("targetType") TargetType targetType);


}
