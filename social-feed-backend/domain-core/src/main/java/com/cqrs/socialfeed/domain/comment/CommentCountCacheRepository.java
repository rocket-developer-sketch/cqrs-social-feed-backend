package com.cqrs.socialfeed.domain.comment;

import java.util.List;
import java.util.Map;

public interface CommentCountCacheRepository {
    void increment(Long postId);
    void decrement(Long postId);
    Long getCount(Long postId);
    Map<Long, Long> getCounts(List<Long> postIds);
    void setCount(Long key, Long value);
}
