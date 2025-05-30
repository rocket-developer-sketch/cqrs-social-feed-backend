package com.cqrs.socialfeed.api.feed;

import java.time.LocalDateTime;

public record GetMyFeed (
        Long postId,
        Long postAuthorId,
         LocalDateTime createdAt,
        LocalDateTime pushedAt,
        String content,
        Long likeCount,
        Long commentCount
) {}
