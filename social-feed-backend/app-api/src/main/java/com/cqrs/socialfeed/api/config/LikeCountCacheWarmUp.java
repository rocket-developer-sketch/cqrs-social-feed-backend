package com.cqrs.socialfeed.api.config;

import com.cqrs.socialfeed.domain.like.LikeCountCacheRepository;
import com.cqrs.socialfeed.domain.like.LikeCountWarmUpCacheRepository;
import com.cqrs.socialfeed.domain.like.LikeRepository;
import com.cqrs.socialfeed.domain.like.TargetType;
import com.cqrs.socialfeed.domain.post.PostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class LikeCountCacheWarmUp {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final LikeCountWarmUpCacheRepository likeCountWarmUpCacheRepository;
    private final LikeCountCacheRepository cacheRepository;

    public LikeCountCacheWarmUp(LikeRepository likeRepository, PostRepository postRepository, LikeCountWarmUpCacheRepository likeCountWarmUpCacheRepository, LikeCountCacheRepository cacheRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.likeCountWarmUpCacheRepository = likeCountWarmUpCacheRepository;
        this.cacheRepository = cacheRepository;
    }

    @PostConstruct
    public void conditionalWarmUp() {
        Boolean alreadyWarmedUp = likeCountWarmUpCacheRepository.hasKey("cache:like_count:preloaded");

        if (Boolean.TRUE.equals(alreadyWarmedUp)) {
            System.out.println("cacheRepository like count already warmed up. Skipping.");
            return;
        }

        System.out.println("cacheRepository like count cache missing. Starting warm-up...");

//        Map<Long, Long> postCounts = repository.getCountsByTargetType(TargetType.POST);
//        postCounts.forEach(cacheRepository::setCount);

//        Map<Long, Long> commentCounts = likeRepository.getCountsByTargetType(TargetType.COMMENT);
//        commentCounts.forEach(cacheRepository::setCount);

        // 최근 60일 기준 게시글 조회
        LocalDateTime cutoff = LocalDateTime.now().minusDays(60);
        List<Long> recentPostIds = postRepository.findPostIdsAfter(cutoff);

        // 게시글 대상 Like 카운트
        Map<Long, Long> postCounts = likeRepository.countByTargetIdsAndType(recentPostIds, TargetType.POST);
        postCounts.forEach((postId, count) -> cacheRepository.setCount(postId, count, TargetType.POST.name()));

        // 게시글 대상 Comment Like 카운트 (optional)
        Map<Long, Long> commentCounts = likeRepository.countByTargetIdsAndType(recentPostIds, TargetType.COMMENT);
        commentCounts.forEach((postId, count) -> cacheRepository.setCount(postId, count,  TargetType.COMMENT.name()));

        // 워밍업 완료 마킹
        likeCountWarmUpCacheRepository.setCacheWarmUpKey();

        System.out.println("cacheRepository like count cache warm-up complete.");
    }
}
