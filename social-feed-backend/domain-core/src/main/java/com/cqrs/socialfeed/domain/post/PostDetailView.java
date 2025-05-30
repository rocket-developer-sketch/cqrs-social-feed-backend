package com.cqrs.socialfeed.domain.post;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailView {
    private final Long postId;
    private final Long authorId;
    private final String content;
    private final List<String> imageUrls;
    private final Long likeCount;
    private final Long commentCount;
    private final LocalDateTime createdAt;

    public PostDetailView(Long postId, Long authorId, String content, List<String> imageUrls, Long likeCount, Long commentCount, LocalDateTime createdAt) {
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.imageUrls = imageUrls;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
