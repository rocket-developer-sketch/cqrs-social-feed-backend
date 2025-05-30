package com.cqrs.socialfeed.command.post.usecase;

import com.cqrs.socialfeed.command.post.command.CreatePostCommand;

public interface CreatePostUseCase {
    Long create(CreatePostCommand command);
}
