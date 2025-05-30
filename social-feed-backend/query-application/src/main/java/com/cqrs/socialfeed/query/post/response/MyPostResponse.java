package com.cqrs.socialfeed.query.post.response;

import java.time.LocalDateTime;

public class MyPostResponse {
    private Long postId;
    private String content;
    private LocalDateTime createdAt;
    private Long likeCount;
    private Long commentCount;

    public MyPostResponse(Long postId, String content, LocalDateTime createdAt, Long likeCount, Long commentCount) {
        this.postId = postId;
        this.content = content;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public Long getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }
}
