package com.cqrs.socialfeed.domain.user;

public class UserProfilePreview {
    private final Long id;
    private final String username;
    private final String profileImageUrl;

    public UserProfilePreview(Long id, String username, String profileImageUrl) {
        this.id = id;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
