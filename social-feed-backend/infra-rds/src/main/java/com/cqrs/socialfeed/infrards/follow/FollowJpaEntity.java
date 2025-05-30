package com.cqrs.socialfeed.infrards.follow;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"followerId", "followeeId"})
})
public class FollowJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long followerId;

    @Column(nullable = false)
    private Long followeeId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected FollowJpaEntity() {}

    public FollowJpaEntity(Long id, Long followerId, Long followeeId, LocalDateTime createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getFollowerId() { return followerId; }
    public Long getFolloweeId() { return followeeId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
