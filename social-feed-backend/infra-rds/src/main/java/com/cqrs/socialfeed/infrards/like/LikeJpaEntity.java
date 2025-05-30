package com.cqrs.socialfeed.infrards.like;


import com.cqrs.socialfeed.domain.like.TargetType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
public class LikeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Long userId;

    @Column(nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected LikeJpaEntity(){}

    public LikeJpaEntity(Long id, Long userId, Long targetId, TargetType targetType, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
