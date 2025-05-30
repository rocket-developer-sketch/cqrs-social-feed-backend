package com.cqrs.socialfeed.infrards.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageJpaRepository extends JpaRepository<PostImageJpaEntity, Long> {
    List<PostImageJpaEntity> findByPostId(Long postId);
}
