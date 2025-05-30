package com.cqrs.socialfeed.domain.like;

public class LikeCount {
    private final Long targetId;
    private final Long count;

    public LikeCount(Long targetId, Long count) {
        this.targetId = targetId;
        this.count = count;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Long getCount() {
        return count;
    }
}
