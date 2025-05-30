package com.cqrs.socialfeed.domain.follow;

import com.cqrs.socialfeed.domain.user.UserProfilePreview;

import java.util.List;

public interface FollowRepository {
    void follow(Follow follow);

    void unfollow(Follow follow);

    boolean isFollowing(Long followerId, Long followeeId);

    List<Long> findFollowerIdsByFolloweeId(Long followeeId);  // 피드 전파용

    List<Long> findFolloweeIdsByFollowerId(Long followerId);  // 내 팔로잉 목록 조회용

    List<UserProfilePreview> findFollowing(Long userId);

    List<UserProfilePreview> findFollowers(Long userId);

    int countFollowing(Long userId);

    int countFollowers(Long userId);
}
