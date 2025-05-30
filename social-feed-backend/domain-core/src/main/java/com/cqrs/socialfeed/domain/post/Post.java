package com.cqrs.socialfeed.domain.post;

import java.time.LocalDateTime;

public class Post {
    private final Long id;
    private final Long userId;
    private final String username;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Post(Long id, Long userId, String username, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUsername() {
        return username;
    }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

}
