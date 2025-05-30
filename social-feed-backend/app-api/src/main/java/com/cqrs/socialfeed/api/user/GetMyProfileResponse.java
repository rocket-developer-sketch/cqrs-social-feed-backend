package com.cqrs.socialfeed.api.user;

public record GetMyProfileResponse(
        Long id,
        String username,
        String profileImageUrl
) {}
