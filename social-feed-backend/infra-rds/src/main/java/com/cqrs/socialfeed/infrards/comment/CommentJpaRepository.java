package com.cqrs.socialfeed.infrards.comment;

import com.cqrs.socialfeed.domain.comment.CommentCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, Long> {
    List<CommentJpaEntity> findAllByPostIdOrderByCreatedAtAsc(Long postId);
    @Query("""
        SELECT new com.cqrs.socialfeed.domain.comment.CommentCount(
            l.postId,
            COUNT(l)
        )
        FROM CommentJpaEntity l
        GROUP BY l.postId
    """)
    List<CommentCount> getCounts();

    @Query("""
        SELECT new com.cqrs.socialfeed.domain.comment.CommentCount(
            l.postId,
            COUNT(l)
        )
        FROM CommentJpaEntity l
        WHERE l.postId IN :postIds
        GROUP BY l.postId
    """)
    List<CommentCount> getCountsByPostIds(@Param("postIds") List<Long> postIds);
}
