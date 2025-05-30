package com.cqrs.socialfeed.query.feed.service;

import com.cqrs.socialfeed.domain.feed.Feed;
import com.cqrs.socialfeed.domain.feed.FeedRepository;
import com.cqrs.socialfeed.domain.like.TargetType;
import com.cqrs.socialfeed.domain.post.Post;
import com.cqrs.socialfeed.domain.post.PostRepository;
import com.cqrs.socialfeed.query.comment.service.CommentServiceQuery;
import com.cqrs.socialfeed.query.feed.request.GetFeedsByUserIdRequest;
import com.cqrs.socialfeed.query.feed.response.FeedResponse;
import com.cqrs.socialfeed.query.feed.usecase.FeedQueryUseCase;
import com.cqrs.socialfeed.query.like.service.LikeQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FeedServiceQuery implements FeedQueryUseCase {
    private final FeedRepository feedRepository;
    private final PostRepository postRepository;
//    private final LikeCountCacheRepository likeCountCacheRepository;
//    private final CommentCountCacheRepository commentCountCacheRepository;
    private final LikeQueryService likeQueryService;
    private final CommentServiceQuery commentQueryService;

    public FeedServiceQuery(FeedRepository feedRepository, PostRepository postRepository,
                            LikeQueryService likeQueryService, CommentServiceQuery commentQueryService) {
        this.feedRepository = feedRepository;
        this.postRepository = postRepository;
        this.likeQueryService = likeQueryService;
        this.commentQueryService = commentQueryService;
    }

//    @Override
//    public List<Feed> getFeeds(GetFeedsByUserIdRequest request) {
//        return feedRepository.findFeedsByUserId(request.getUserId(), request.getCursorPostId(), request.getSize());
//    }


    /**
     * 로그인한 사용자(userId)의 피드 수신함에 푸시된 게시글 중, 커서 이후의 최신 글을 페이징 조회
     * */
//    @Override
//    public List<FeedResponse> getFeeds(GetFeedsByUserIdRequest request) {
//        List<Feed> feeds = feedRepository.findFeedsByUserId(request.getUserId(), request.getCursorPostId(), request.getSize());
//
//        return feeds.stream().map(feed -> {
//            Long postId = feed.getPostId();
//            Long likeCount = likeCountCacheRepository.getCount(postId);
//            Long commentCount = commentCountCacheRepository.getCount(postId);
//
//            return new FeedResponse(
//                    postId,
//                    feed.getPostAuthorId(),
//                    feed.getCreatedAt(),
//                    feed.getPushedAt(),
//                    likeCount,
//                    commentCount
//            );
//        }).collect(Collectors.toList());
//    }

    @Override
    public List<FeedResponse> getFeeds(GetFeedsByUserIdRequest request) {
        // MongoDB에서 피드 전파 내역 조회
        List<Feed> feeds = feedRepository.findFeedsByUserId(request.getUserId(), request.getCursorPostId(), request.getSize());

        List<Long> postIds = feeds.stream()
                .map(Feed::getPostId)
                .collect(Collectors.toList());

        // RDB에서 게시글 본문 title/content 한 번에 조회
        Map<Long, Post> postsById = postRepository.findByIds(postIds)
                .stream()
                .collect(Collectors.toMap(Post::getId, Function.identity()));

        // Redis 캐시 조회 (like/comment)
//        Map<Long, Long> likeCounts = likeCountCacheRepository.getCounts(postIds);
//        Map<Long, Long> commentCounts = commentCountCacheRepository.getCounts(postIds);

        Map<Long, Long> likeCounts = likeQueryService.getCountsForPostWithFallback(postIds, TargetType.POST);
        Map<Long, Long> commentCounts = commentQueryService.getCountsWithFallback(postIds);

        return feeds.stream()
                .map(feed -> {
                    Long postId = feed.getPostId();
                    Post post = postsById.get(postId);

                    return new FeedResponse(
                            postId,
                            feed.getPostAuthorId(),
                            feed.getCreatedAt(),
                            feed.getPushedAt(),
                            post.getContent(),
                            likeCounts.getOrDefault(postId, 0L),
                            commentCounts.getOrDefault(postId, 0L)
                    );
                })
                .collect(Collectors.toList());
    }
}
