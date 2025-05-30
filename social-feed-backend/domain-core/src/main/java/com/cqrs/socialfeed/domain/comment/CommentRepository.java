package com.cqrs.socialfeed.domain.comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentRepository {
    Long save(Comment comment);
    List<Comment> findAllByPostIdOrderByCreatedAtAsc(Long postId);
    Optional<Comment> findById(Long commentId);

    Map<Long, Long> getCounts();
    Map<Long, Long> getCountsByPostIds(List<Long> postIds);
}
