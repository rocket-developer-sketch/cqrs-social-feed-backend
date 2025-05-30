package com.cqrs.socialfeed.api.like;

import com.cqrs.socialfeed.domain.like.TargetType;

public record ToggleLikeRequest(
        Long targetId,
        TargetType targetType
){}
