package com.cqrs.socialfeed.command.comment.usecase;


import com.cqrs.socialfeed.command.comment.command.CreateCommentCommand;
import com.cqrs.socialfeed.command.comment.response.CommentCreateResponse;

public interface CreateCommentUseCase {
    CommentCreateResponse create(CreateCommentCommand command, String locale);
}
