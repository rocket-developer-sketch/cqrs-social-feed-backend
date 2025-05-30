package com.cqrs.socialfeed.domain.comment;

public interface CommentCountWarmUpCacheRepository {
    void setCacheWarmUpKey();

    Boolean hasKey(String key);
}
