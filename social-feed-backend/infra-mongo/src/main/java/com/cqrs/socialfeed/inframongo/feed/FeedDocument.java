package com.cqrs.socialfeed.inframongo.feed;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "feeds")
public class FeedDocument {
    @Id
    private String id;
    private Long userId;
    private Long postId;
    private Long postAuthorId;
    private LocalDateTime createdAt;
    private LocalDateTime pushedAt;

    protected FeedDocument() {}

    public FeedDocument(Long userId, Long postId, Long postAuthorId, LocalDateTime createdAt, LocalDateTime pushedAt) {
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
