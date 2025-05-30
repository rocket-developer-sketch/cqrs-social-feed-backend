package com.cqrs.socialfeed.infrards.post;


import com.cqrs.socialfeed.domain.post.PostImage;
import com.cqrs.socialfeed.domain.post.PostImageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostImageRepositoryImpl implements PostImageRepository {

    private final PostImageJpaRepository jpaRepository;

    public PostImageRepositoryImpl(PostImageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(PostImage postImage) {
        PostImageJpaEntity entity = new PostImageJpaEntity(
                postImage.getId(),
                postImage.getPostId(),
                postImage.getImageUrl(),
                postImage.getOrder()
        );
        jpaRepository.save(entity);
    }

    @Override
    public List<PostImage> findByPostId(Long postId) {
        return jpaRepository.findByPostId(postId).stream()
                .map(entity -> new PostImage(
                        entity.getId(),
                        entity.getPostId(),
                        entity.getImageUrl(),
                        entity.getDisplayOrder()
                ))
                .collect(Collectors.toList());
    }
}
