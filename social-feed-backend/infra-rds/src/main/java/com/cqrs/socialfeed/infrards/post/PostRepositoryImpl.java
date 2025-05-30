package com.cqrs.socialfeed.infrards.post;

import com.cqrs.socialfeed.domain.post.Post;
import com.cqrs.socialfeed.domain.post.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository jpaRepository;

    public PostRepositoryImpl(PostJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Long save(Post post) {
        PostJpaEntity entity = new PostJpaEntity(
                post.getId(),
                post.getUserId(),
                post.getUsername(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
        return jpaRepository.save(entity).getId();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return jpaRepository.findById(id)
                .map(entity -> new Post(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getUsername(),
                        entity.getContent(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt()
                ));
    }

    @Override
    public List<Post> findMyPosts(Long userId, Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);
        return jpaRepository.findMyPosts(userId, cursorId, pageable).stream()
                .map(e-> new Post(e.getId(), e.getUserId(), e.getUsername(), e.getContent(), e.getCreatedAt(), e.getUpdatedAt())).toList();
    }

    @Override
    public List<Post> findByIds(List<Long> postIds) {
        return jpaRepository.findAllById(postIds).stream().map(e-> new Post(e.getId(), e.getUserId(), e.getUsername(), e.getContent(), e.getCreatedAt(), e.getUpdatedAt())).toList();
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }

    @Override
    public List<Long> findPostIdsAfter(LocalDateTime cutoff) {
        return jpaRepository.findPostIdsAfter(cutoff);
    }
}
