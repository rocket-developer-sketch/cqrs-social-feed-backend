package com.cqrs.socialfeed.command.follow.usercase;

import com.cqrs.socialfeed.command.follow.command.FollowUserCommand;

public interface FollowUserUseCase {
    void follow(FollowUserCommand command);
}
