package com.cqrs.socialfeed.query.follow.response;

public class FollowStatsResponse {
        private final int followerCount;
        private final int followingCount;

    public FollowStatsResponse(int followerCount, int followingCount) {
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }
}
