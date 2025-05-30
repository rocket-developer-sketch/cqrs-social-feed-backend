package com.cqrs.socialfeed.domain.auth;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    Optional<RefreshToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
