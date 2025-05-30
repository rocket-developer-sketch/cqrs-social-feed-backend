package com.cqrs.socialfeed.domain.comment;

public class CommentCount {
    private final Long postId;
    private final Long count;

    public CommentCount(Long postId, Long count) {
        this.postId = postId;
        this.count = count;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getCount() {
        return count;
    }
}
