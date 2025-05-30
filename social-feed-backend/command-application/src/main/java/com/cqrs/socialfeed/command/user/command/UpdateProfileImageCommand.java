package com.cqrs.socialfeed.command.user.command;

public class UpdateProfileImageCommand {
    private final Long userId;
    private final String imageUrl;

    public UpdateProfileImageCommand(Long userId, String imageUrl) {
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
