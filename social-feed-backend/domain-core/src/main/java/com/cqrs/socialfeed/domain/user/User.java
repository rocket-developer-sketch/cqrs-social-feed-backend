package com.cqrs.socialfeed.domain.user;

import java.time.LocalDateTime;

public class User {
    private final Long id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private String profileImageUrl;
    private final String bio;
    private final LocalDateTime createdAt;

    public User(Long id, String username, String email, String passwordHash,
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

    public void updateProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }
}
