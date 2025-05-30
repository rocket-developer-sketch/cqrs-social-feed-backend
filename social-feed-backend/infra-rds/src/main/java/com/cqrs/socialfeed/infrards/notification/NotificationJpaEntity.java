package com.cqrs.socialfeed.infrards.notification;


import com.cqrs.socialfeed.domain.notification.NotificationType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class NotificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private boolean read;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected NotificationJpaEntity() {}

    public NotificationJpaEntity(Long id, Long userId, String content, NotificationType type, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.type = type;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getContent() { return content; }
    public NotificationType getType() { return type; }
    public boolean isRead() { return read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
