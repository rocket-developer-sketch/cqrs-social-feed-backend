package com.cqrs.socialfeed.api.post;

import java.time.LocalDateTime;

public record GetMyPostResponse (
        Long postId,
        String content,
        LocalDateTime createdAt,
        Long likeCount,
        Long commentCount
) {}
