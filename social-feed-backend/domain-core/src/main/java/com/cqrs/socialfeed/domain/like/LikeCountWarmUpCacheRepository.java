package com.cqrs.socialfeed.domain.like;

public interface LikeCountWarmUpCacheRepository {
    Boolean hasKey(String key);
    void setCacheWarmUpKey();
}
