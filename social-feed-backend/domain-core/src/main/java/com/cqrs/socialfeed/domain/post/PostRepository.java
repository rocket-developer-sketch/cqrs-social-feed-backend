package com.cqrs.socialfeed.domain.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Long save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findMyPosts(Long userId, Long cursorId, int size);
    List<Post> findByIds(List<Long> postIds);
    long countByUserId(Long userId);
    List<Long> findPostIdsAfter(LocalDateTime cutoff);
}
