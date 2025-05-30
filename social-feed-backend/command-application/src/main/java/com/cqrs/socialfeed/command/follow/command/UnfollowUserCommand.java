package com.cqrs.socialfeed.command.follow.command;

public class UnfollowUserCommand {
    private final Long followerId;
    private final Long followeeId;

    public UnfollowUserCommand(Long followerId, Long followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public Long getFolloweeId() {
        return followeeId;
    }
}
