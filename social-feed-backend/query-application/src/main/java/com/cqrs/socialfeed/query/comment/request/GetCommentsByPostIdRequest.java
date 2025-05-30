package com.cqrs.socialfeed.query.comment.request;

public class GetCommentsByPostIdRequest {
    private final Long postId;

    public GetCommentsByPostIdRequest(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

}
