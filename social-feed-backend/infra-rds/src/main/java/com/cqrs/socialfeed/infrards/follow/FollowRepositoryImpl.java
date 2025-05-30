package com.cqrs.socialfeed.infrards.follow;

import com.cqrs.socialfeed.domain.follow.Follow;
import com.cqrs.socialfeed.domain.follow.FollowRepository;
import com.cqrs.socialfeed.domain.user.UserProfilePreview;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FollowRepositoryImpl implements FollowRepository {

    private final FollowJpaRepository jpaRepository;

    public FollowRepositoryImpl(FollowJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void follow(Follow follow) {
        if (!jpaRepository.existsByFollowerIdAndFolloweeId(follow.getFollowerId(), follow.getFolloweeId())) {
            FollowJpaEntity entity = new FollowJpaEntity(null, follow.getFollowerId(), follow.getFolloweeId(), LocalDateTime.now());
            jpaRepository.save(entity);
        }
    }

    @Override
    public void unfollow(Follow follow) {
        jpaRepository.deleteByFollowerIdAndFolloweeId(follow.getFollowerId(), follow.getFolloweeId());
    }

    @Override
    public boolean isFollowing(Long followerId, Long followeeId) {
        return jpaRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Override
    public List<Long> findFollowerIdsByFolloweeId(Long followeeId) {
        return jpaRepository.findByFolloweeId(followeeId).stream()
                .map(FollowJpaEntity::getFollowerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findFolloweeIdsByFollowerId(Long followerId) {
        return jpaRepository.findByFollowerId(followerId).stream()
                .map(FollowJpaEntity::getFolloweeId)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserProfilePreview> findFollowing(Long userId) {
        return jpaRepository.findFollowing(userId);
    }

    @Override
    public List<UserProfilePreview> findFollowers(Long userId) {
        return jpaRepository.findFollowers(userId);
    }

    @Override
    public int countFollowing(Long userId) {
        return jpaRepository.countByFolloweeId(userId);
    }

    @Override
    public int countFollowers(Long userId) {
        return jpaRepository.countByFollowerId(userId);
    }
}
