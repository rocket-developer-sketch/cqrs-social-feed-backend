package com.cqrs.socialfeed.query.feed.response;

import java.time.LocalDateTime;

public class FeedResponse {
    private Long postId;
    private Long postAuthorId;
    private LocalDateTime createdAt;
    private LocalDateTime pushedAt;
    private String content;
    private Long likeCount;
    private Long commentCount;

    public FeedResponse(Long postId, Long postAuthorId, LocalDateTime createdAt, LocalDateTime pushedAt,
                        String content, Long likeCount, Long commentCount) {
        this.postId = postId;
        this.postAuthorId = postAuthorId;
        this.createdAt = createdAt;
        this.pushedAt = pushedAt;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getPostAuthorId() {
        return postAuthorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPushedAt() {
        return pushedAt;
    }

    public String getContent() {
        return content;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }
}
