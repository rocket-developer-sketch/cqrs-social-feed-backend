package com.cqrs.socialfeed.query.follow.service;

import com.cqrs.socialfeed.domain.follow.FollowRepository;
import com.cqrs.socialfeed.query.follow.response.FollowStatsResponse;
import com.cqrs.socialfeed.query.follow.response.UserSummaryResponse;
import com.cqrs.socialfeed.query.follow.usecase.FollowQueryUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowQueryService implements FollowQueryUseCase {

    private final FollowRepository followRepository;

    public FollowQueryService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Override
    public FollowStatsResponse getStats(Long userId) {
        int followerCount = followRepository.countFollowers(userId);
        int followingCount = followRepository.countFollowing(userId);
        return new FollowStatsResponse(followerCount, followingCount);
    }

    @Override
    public List<UserSummaryResponse> getFollowing(Long userId) {
        return followRepository.findFollowing(userId).stream().map(e ->
                new UserSummaryResponse(e.getId(), e.getUsername(), e.getProfileImageUrl())
        ).toList();
    }

    @Override
    public List<UserSummaryResponse> getFollowers(Long userId) {
        return followRepository.findFollowers(userId).stream().map(e ->
                new UserSummaryResponse(e.getId(), e.getUsername(), e.getProfileImageUrl())
        ).toList();
    }

    @Override
    public boolean isFollowing(Long followerId, Long followeeId) {
        return followRepository.isFollowing(followerId, followeeId);
    }
}
