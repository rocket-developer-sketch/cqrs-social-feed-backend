package com.cqrs.socialfeed.api.post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        Long postId,
        Long authorId,
        String content,
        List<String> imageUrls,
        Long likeCount,
        Long commentCount,
        LocalDateTime createdAt
) {}