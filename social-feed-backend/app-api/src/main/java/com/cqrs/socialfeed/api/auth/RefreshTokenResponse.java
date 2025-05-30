package com.cqrs.socialfeed.api.auth;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {}