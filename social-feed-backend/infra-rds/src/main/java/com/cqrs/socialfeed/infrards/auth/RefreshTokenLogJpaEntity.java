package com.cqrs.socialfeed.infrards.auth;

import com.cqrs.socialfeed.domain.auth.RefreshTokenLogResultType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token_log")
public class RefreshTokenLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime usedAt;

    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    @Enumerated(EnumType.STRING)
    private RefreshTokenLogResultType result;

    protected RefreshTokenLogJpaEntity(){}

    public RefreshTokenLogJpaEntity(Long userId, LocalDateTime usedAt, String ipAddress, String userAgent, RefreshTokenLogResultType result) {
        this.userId = userId;
        this.usedAt = usedAt;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public RefreshTokenLogResultType getResult() {
        return result;
    }
}
