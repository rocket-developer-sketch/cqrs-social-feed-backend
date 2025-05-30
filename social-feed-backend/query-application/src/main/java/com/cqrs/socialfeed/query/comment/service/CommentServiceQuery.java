package com.cqrs.socialfeed.query.comment.service;

import com.cqrs.socialfeed.domain.comment.CommentCountCacheRepository;
import com.cqrs.socialfeed.query.comment.request.GetCommentsByPostIdRequest;
import com.cqrs.socialfeed.query.comment.usecase.CommentQueryUseCase;
import com.cqrs.socialfeed.domain.comment.Comment;
import com.cqrs.socialfeed.domain.comment.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceQuery implements CommentQueryUseCase {

    private final CommentRepository commentRepository;
    private final CommentCountCacheRepository cacheRepository;

    public CommentServiceQuery(CommentRepository commentRepository, CommentCountCacheRepository cacheRepository) {
        this.commentRepository = commentRepository;
        this.cacheRepository = cacheRepository;
    }

    @Override
    public List<Comment> getCommentsByPostId(GetCommentsByPostIdRequest command) {
        return commentRepository.findAllByPostIdOrderByCreatedAtAsc(command.getPostId());
    }


    @Override
    public Map<Long, Long> getCountsWithFallback(List<Long> postIds) {
        Map<Long, Long> cached = cacheRepository.getCounts(postIds);

        // fallback 대상 필터링
        List<Long> missingIds = postIds.stream()
                .filter(id -> cached.get(id) == null)
                .toList();

        Map<Long, Long> fallback = new HashMap<>();
        if (!missingIds.isEmpty()) {
            fallback = commentRepository.getCountsByPostIds(missingIds);

            // Redis warm-up
            for (Map.Entry<Long, Long> entry : fallback.entrySet()) {
                cacheRepository.setCount(entry.getKey(), entry.getValue());
            }
        }

        // 최종 병합: Redis에 있던 값 + fallback 값
        Map<Long, Long> result = new HashMap<>(cached);
        result.putAll(fallback);

        return result;
    }
}
