package com.cqrs.socialfeed.infrards.user;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(length = 255)
    private String bio;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected UserJpaEntity(){}

    public UserJpaEntity(Long id, String username, String email, String passwordHash,
                      String profileImageUrl, String bio, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
