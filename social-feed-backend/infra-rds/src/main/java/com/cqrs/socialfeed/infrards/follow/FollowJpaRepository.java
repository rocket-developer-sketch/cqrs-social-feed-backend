package com.cqrs.socialfeed.infrards.follow;


import com.cqrs.socialfeed.domain.user.UserProfilePreview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface FollowJpaRepository extends JpaRepository<FollowJpaEntity, Long> {
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
    List<FollowJpaEntity> findByFolloweeId(Long followeeId);
    List<FollowJpaEntity> findByFollowerId(Long followerId);
    int countByFollowerId(Long userId); // 내가 팔로우한 사람 수
    int countByFolloweeId(Long userId); // 나를 팔로우한 사람 수

    @Query("""
    SELECT new com.cqrs.socialfeed.domain.user.UserProfilePreview(
        u.id, u.username, u.profileImageUrl
    )
    FROM FollowJpaEntity f
    JOIN UserJpaEntity u ON u.id = f.followeeId
    WHERE f.followerId = :userId
""")
    List<UserProfilePreview> findFollowing(@Param("userId") Long userId);

    @Query("""
    SELECT new com.cqrs.socialfeed.domain.user.UserProfilePreview(
        u.id, u.username, u.profileImageUrl
    )
    FROM FollowJpaEntity f
    JOIN UserJpaEntity u ON u.id = f.followeeId
    WHERE f.followeeId = :userId
""")
    List<UserProfilePreview> findFollowers(@Param("userId") Long userId);
}
