package com.cqrs.socialfeed.query.like.usecase;

import com.cqrs.socialfeed.domain.like.TargetType;

import java.util.List;
import java.util.Map;

public interface LikeQueryUseCase {
    Map<Long, Long> getCountsForPostWithFallback(List<Long> postIds, TargetType targetType);
}
