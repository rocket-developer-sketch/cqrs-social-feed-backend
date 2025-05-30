package com.cqrs.socialfeed.api.comment;

public record CreateCommentRequest (
    Long postId,
    Long parentId, // nullable
    String content
){}
