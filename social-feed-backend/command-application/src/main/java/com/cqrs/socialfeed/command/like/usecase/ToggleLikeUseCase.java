package com.cqrs.socialfeed.command.like.usecase;

import com.cqrs.socialfeed.command.like.command.ToggleLikeCommand;
import com.cqrs.socialfeed.command.like.response.LikeToggleResponse;

public interface ToggleLikeUseCase {
    LikeToggleResponse toogleLike(ToggleLikeCommand command);
}
