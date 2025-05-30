package com.cqrs.socialfeed.api.user;

public record GetMyFollowStatResponse (
        int followerCount,
        int followingCount
) {}
