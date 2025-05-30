package com.cqrs.socialfeed.domain.comment.messaging;

public class CommentCreatedEvent {
    private Long postId;
    private Long userId;
    private String content;
    private String createdAt; // ISO string

    protected CommentCreatedEvent() {
    }

    public CommentCreatedEvent(Long postId, Long userId, String content, String createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}


