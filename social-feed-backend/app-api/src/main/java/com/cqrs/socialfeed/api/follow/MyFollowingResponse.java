package com.cqrs.socialfeed.api.follow;

public record MyFollowingResponse(
        Long id,
        String username,
        String profileImageUrl
) {}
