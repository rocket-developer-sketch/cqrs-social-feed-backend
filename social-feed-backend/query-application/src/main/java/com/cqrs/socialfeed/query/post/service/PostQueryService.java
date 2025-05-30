package com.cqrs.socialfeed.query.post.service;

import com.cqrs.socialfeed.domain.like.TargetType;
import com.cqrs.socialfeed.domain.post.*;
import com.cqrs.socialfeed.query.comment.service.CommentServiceQuery;
import com.cqrs.socialfeed.query.like.service.LikeQueryService;
import com.cqrs.socialfeed.query.post.request.GetMyPostsRequest;
import com.cqrs.socialfeed.query.post.response.MyPostResponse;
import com.cqrs.socialfeed.query.post.usecase.PostQueryUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostQueryService implements PostQueryUseCase {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final LikeQueryService likeQueryService;
    private final CommentServiceQuery commentQueryService;

    public PostQueryService(PostRepository postRepository, PostImageRepository postImageRepository, LikeQueryService likeQueryService,
                            CommentServiceQuery commentQueryService) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        this.likeQueryService = likeQueryService;
        this.commentQueryService = commentQueryService;
    }

    @Override
    public PostDetailView getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        List<String> imageUrls = postImageRepository.findByPostId(postId).stream()
                .map(PostImage::getImageUrl)
                .toList();

        return new PostDetailView(
                post.getId(),
                post.getUserId(), // author
                post.getContent(),
                imageUrls,
                likeQueryService.getCountsForPostWithFallback(List.of(postId), TargetType.POST).getOrDefault(postId, 0L),
                commentQueryService.getCountsWithFallback(List.of(postId)).getOrDefault(postId, 0L),
                post.getCreatedAt()
        );
    }

    @Override
    public List<MyPostResponse> getMyPosts(GetMyPostsRequest request) {
        List<Post> posts = postRepository.findMyPosts(
                request.getUserId(),
                request.getCursorPostId(),
                request.getSize()
        );

        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        //Map<Long, Long> likeCounts = likeCountCacheRepository.getCounts(postIds);
        //Map<Long, Long> commentCounts = commentCountCacheRepository.getCounts(postIds);
        Map<Long, Long> likeCounts = likeQueryService.getCountsForPostWithFallback(postIds, TargetType.POST);
        Map<Long, Long> commentCounts = commentQueryService.getCountsWithFallback(postIds);

        return posts.stream().map(post -> {
            Long postId = post.getId();
            return new MyPostResponse(
                    postId,
                    post.getContent(),
                    post.getCreatedAt(),
                    likeCounts.getOrDefault(postId, 0L),
                    commentCounts.getOrDefault(postId, 0L)
            );
        }).collect(Collectors.toList());
    }
}
