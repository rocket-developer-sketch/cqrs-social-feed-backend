package com.cqrs.socialfeed.domain.post;

import java.util.List;

public interface PostImageRepository {
    void save(PostImage postImage);
    List<PostImage> findByPostId(Long postId);
}
