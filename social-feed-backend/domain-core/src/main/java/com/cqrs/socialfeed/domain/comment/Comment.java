package com.cqrs.socialfeed.domain.comment;

import java.time.LocalDateTime;

public class Comment {
    private final Long id;
    private final Long postId;
    private final Long userId;
    private final Long parentId; // nullable
    private final String content;
    private final LocalDateTime createdAt;

    public Comment(Long id, Long postId, Long userId, Long parentId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
