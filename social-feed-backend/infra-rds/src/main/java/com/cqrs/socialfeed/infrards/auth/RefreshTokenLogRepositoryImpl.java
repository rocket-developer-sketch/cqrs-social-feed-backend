package com.cqrs.socialfeed.infrards.auth;

import com.cqrs.socialfeed.domain.auth.RefreshTokenLog;
import com.cqrs.socialfeed.domain.auth.RefreshTokenLogRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenLogRepositoryImpl implements RefreshTokenLogRepository {

    private final RefreshTokenLogJpaRepository jpaRepository;

    public RefreshTokenLogRepositoryImpl(RefreshTokenLogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(RefreshTokenLog log) {
        jpaRepository.save(new RefreshTokenLogJpaEntity(
                log.getUserId(),
                log.getUsedAt(),
                log.getIpAddress(),
                log.getUserAgent(),
                log.getResult()
        ));
    }
}
