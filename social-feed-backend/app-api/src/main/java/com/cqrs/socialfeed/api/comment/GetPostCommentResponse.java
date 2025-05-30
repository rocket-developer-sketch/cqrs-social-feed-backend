package com.cqrs.socialfeed.api.comment;

import java.time.LocalDateTime;

public record GetPostCommentResponse(
        Long id,
        Long postId,
        Long userId,
        Long parentId, // nullable
        String content,
        LocalDateTime createdAt
) {}
