package com.cqrs.socialfeed.query.comment.usecase;

import com.cqrs.socialfeed.query.comment.request.GetCommentsByPostIdRequest;
import com.cqrs.socialfeed.domain.comment.Comment;

import java.util.List;
import java.util.Map;

public interface CommentQueryUseCase {
    List<Comment> getCommentsByPostId(GetCommentsByPostIdRequest command);
    Map<Long, Long> getCountsWithFallback(List<Long> postIds);
}
