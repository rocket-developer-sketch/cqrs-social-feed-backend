package com.cqrs.socialfeed.infrards.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenLogJpaRepository extends JpaRepository<RefreshTokenLogJpaEntity, Long> {
}

