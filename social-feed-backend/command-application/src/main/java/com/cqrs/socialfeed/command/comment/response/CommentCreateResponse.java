package com.cqrs.socialfeed.command.comment.response;

public class CommentCreateResponse {
    private final Long commentId;

    public CommentCreateResponse(Long commentId/*, long commentCount*/) {
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}
