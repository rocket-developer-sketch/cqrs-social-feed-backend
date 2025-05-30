package com.cqrs.socialfeed.command.post.command;

import java.util.List;

public class CreatePostCommand {
    private final Long userId;
    private final String username;
    private final String content;
    private final List<String> imageUrls;
    private final String locale;

    public CreatePostCommand(Long userId, String username, String content, List<String> imageUrls, String locale) {
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.imageUrls = imageUrls;
        this.locale = locale;
    }

    public Long getUserId() { return userId; }
    public String getUsername() {
        return username;
    }
    public String getContent() { return content; }
    public List<String> getImageUrls() { return imageUrls; }
    public String getLocale() {
        return locale;
    }

}
