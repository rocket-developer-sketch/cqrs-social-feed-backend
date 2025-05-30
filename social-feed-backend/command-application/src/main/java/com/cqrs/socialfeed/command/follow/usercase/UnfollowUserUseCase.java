package com.cqrs.socialfeed.command.follow.usercase;

import com.cqrs.socialfeed.command.follow.command.UnfollowUserCommand;

public interface UnfollowUserUseCase {
    void unfollow(UnfollowUserCommand command);
}
