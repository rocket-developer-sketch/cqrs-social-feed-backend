package com.cqrs.socialfeed.query.post.usecase;

import com.cqrs.socialfeed.domain.post.PostDetailView;
import com.cqrs.socialfeed.query.post.request.GetMyPostsRequest;
import com.cqrs.socialfeed.query.post.response.MyPostResponse;

import java.util.List;

public interface PostQueryUseCase {
    PostDetailView getPostDetail(Long postId);
    List<MyPostResponse> getMyPosts(GetMyPostsRequest request);
}
