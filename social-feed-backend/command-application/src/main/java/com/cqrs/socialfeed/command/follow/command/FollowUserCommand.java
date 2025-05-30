package com.cqrs.socialfeed.command.follow.command;

public class FollowUserCommand {
    private final Long followerId;
    private final Long followeeId;
    private final String locale;

    public FollowUserCommand(Long followerId, Long followeeId, String locale) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.locale = locale;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public Long getFolloweeId() {
        return followeeId;
    }

    public String getLocale() {
        return locale;
    }
}
