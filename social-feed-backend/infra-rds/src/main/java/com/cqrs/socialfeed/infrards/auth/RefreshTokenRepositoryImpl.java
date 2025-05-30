package com.cqrs.socialfeed.infrards.auth;

import com.cqrs.socialfeed.domain.auth.RefreshToken;
import com.cqrs.socialfeed.domain.auth.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;

    public RefreshTokenRepositoryImpl(RefreshTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(RefreshToken token) {
        jpaRepository.save(new RefreshTokenJpaEntity(
                token.getUserId(),
                token.getToken(),
                token.getExpiresAt()
        ));
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return jpaRepository.findById(userId)
                .map(entity -> new RefreshToken(
                        entity.getUserId(),
                        entity.getToken(),
                        entity.getExpiresAt()
                ));
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpaRepository.deleteById(userId);
    }
}
