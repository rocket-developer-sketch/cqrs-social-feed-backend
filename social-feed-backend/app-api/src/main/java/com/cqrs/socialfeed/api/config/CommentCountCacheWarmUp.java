package com.cqrs.socialfeed.api.config;

import com.cqrs.socialfeed.domain.comment.CommentCountCacheRepository;
import com.cqrs.socialfeed.domain.comment.CommentCountWarmUpCacheRepository;
import com.cqrs.socialfeed.domain.comment.CommentRepository;
import com.cqrs.socialfeed.domain.post.PostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class CommentCountCacheWarmUp {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentCountWarmUpCacheRepository cacheWarmUpRepository;
    private final CommentCountCacheRepository cacheRepository;

    public CommentCountCacheWarmUp(CommentRepository commentRepository, PostRepository postRepository, CommentCountWarmUpCacheRepository cacheWarmUpRepository, CommentCountCacheRepository cacheRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.cacheWarmUpRepository = cacheWarmUpRepository;
        this.cacheRepository = cacheRepository;
    }

    @PostConstruct
    public void conditionalWarmUp() {
        Boolean alreadyWarmedUp = cacheWarmUpRepository.hasKey("cache:comment_count:preloaded");

        // RDB에서 댓글이 달린 postId별 count만 조회
//        Map<Long, Long> commentCounts = commentRepository.getCounts();

        // Redis에 저장
//        commentCounts.forEach(cacheRepository::setCount);

        // 최근 60일 게시글 ID 조회
        LocalDateTime cutoff = LocalDateTime.now().minusDays(60);
        List<Long> recentPostIds = postRepository.findPostIdsAfter(cutoff);

        // 해당 postId에 달린 댓글 수 조회
        Map<Long, Long> commentCounts = commentRepository.getCountsByPostIds(recentPostIds);

        // Redis에 저장
        commentCounts.forEach(cacheRepository::setCount);

        // 워밍업 완료 마킹
        cacheWarmUpRepository.setCacheWarmUpKey();

        System.out.println("cacheRepository comment count cache warm-up complete.");
    }
}
