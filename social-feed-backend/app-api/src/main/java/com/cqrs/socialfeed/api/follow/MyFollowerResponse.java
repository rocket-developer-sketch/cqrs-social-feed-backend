package com.cqrs.socialfeed.api.follow;

public record MyFollowerResponse(
        Long id,
        String username,
        String profileImageUrl
) {}
