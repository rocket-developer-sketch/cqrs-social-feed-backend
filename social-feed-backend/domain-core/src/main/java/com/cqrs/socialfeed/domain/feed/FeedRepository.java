package com.cqrs.socialfeed.domain.feed;

import java.util.List;

public interface FeedRepository {
    void save(Feed feed);
    /**
     * 팔로잉 피드 조회
     * @param receiverId 피드를 받는 사용자 ID
     * @param cursorId 커서 기준 ID (null이면 최신부터)
     * @param size 최대 조회 개수
     * @return Feed 목록
     */
    List<Feed> findFeedsByUserId(Long receiverId, Long cursorId, int size);
}
