package com.cqrs.socialfeed.query.follow.response;

public class UserSummaryResponse {
    private Long id;
    private String username;
    private String profileImageUrl;

    public UserSummaryResponse(Long id, String username, String profileImageUrl) {
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