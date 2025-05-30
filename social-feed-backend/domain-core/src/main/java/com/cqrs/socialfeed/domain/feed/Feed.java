package com.cqrs.socialfeed.domain.feed;

import java.time.LocalDateTime;

public class Feed {
    private final Long userId; // 피드 받을 사용자
    private final Long postId;
    private final Long postAuthorId;
    private final LocalDateTime createdAt;
    private final LocalDateTime pushedAt;

    public Feed(Long userId, Long postId, Long postAuthorId, LocalDateTime createdAt, LocalDateTime pushedAt) {
        this.userId = userId;
        this.postId = postId;
        this.postAuthorId = postAuthorId;
        this.createdAt = createdAt;
        this.pushedAt = pushedAt;
    }

    public Long getUserId() { return userId; }
    public Long getPostId() { return postId; }
    public Long getPostAuthorId() { return postAuthorId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getPushedAt() { return pushedAt; }
}
