package com.cqrs.socialfeed.domain.auth;

public interface RefreshTokenLogRepository {
    void save(RefreshTokenLog log);
}
