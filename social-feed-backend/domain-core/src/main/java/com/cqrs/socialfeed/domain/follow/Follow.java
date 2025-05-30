package com.cqrs.socialfeed.domain.follow;

import java.time.LocalDateTime;

public class Follow {
    private final Long id;
    private final Long followerId;
    private final Long followeeId;
    private final LocalDateTime createdAt;

    public Follow(Long id, Long followerId, Long followeeId, LocalDateTime createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public Long getFolloweeId() {
        return followeeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
