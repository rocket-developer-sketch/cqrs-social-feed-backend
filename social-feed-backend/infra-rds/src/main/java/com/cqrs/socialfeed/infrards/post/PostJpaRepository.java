package com.cqrs.socialfeed.infrards.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Long> {
    List<PostJpaEntity> findByUserId(Long userId, Pageable pageable);

    long countByUserId(Long userId);

    @Query("""
        SELECT p
          FROM PostJpaEntity p
          WHERE p.userId = :userId
            AND (:cursorId IS NULL OR p.id < :cursorId)
          ORDER BY p.id DESC
""")
    List<PostJpaEntity> findMyPosts( @Param("userId") Long userId,
                                     @Param("cursorId") Long cursorId,
                                     Pageable pageable);

    @Query("SELECT p.id FROM PostJpaEntity p WHERE p.createdAt >= :cutoff")
    List<Long> findPostIdsAfter(@Param("cutoff") LocalDateTime cutoff);
}

