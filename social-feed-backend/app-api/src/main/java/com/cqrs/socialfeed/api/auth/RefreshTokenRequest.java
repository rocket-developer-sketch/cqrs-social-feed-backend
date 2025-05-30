package com.cqrs.socialfeed.api.auth;

public record RefreshTokenRequest(
        String refreshToken
) {}