package com.cqrs.socialfeed.query.follow.usecase;

import com.cqrs.socialfeed.query.follow.response.FollowStatsResponse;
import com.cqrs.socialfeed.query.follow.response.UserSummaryResponse;

import java.util.List;

public interface FollowQueryUseCase {
    FollowStatsResponse getStats(Long userId);

    List<UserSummaryResponse> getFollowing(Long id);

    List<UserSummaryResponse> getFollowers(Long id);

    boolean isFollowing(Long followerId, Long followeeId);
}
