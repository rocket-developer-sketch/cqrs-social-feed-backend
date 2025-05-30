package com.cqrs.socialfeed.infrards.comment;

import com.cqrs.socialfeed.domain.comment.Comment;
import com.cqrs.socialfeed.domain.comment.CommentCount;
import com.cqrs.socialfeed.domain.comment.CommentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository jpaRepository;

    public CommentRepositoryImpl(CommentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Long save(Comment comment) {
        CommentJpaEntity entity = new CommentJpaEntity(
                comment.getId(),
                comment.getPostId(),
                comment.getUserId(),
                comment.getParentId(),
                comment.getContent(),
                comment.getCreatedAt()
        );
        return jpaRepository.save(entity).getId();
    }

    @Override
    public List<Comment> findAllByPostIdOrderByCreatedAtAsc(Long postId) {
        return jpaRepository.findAllByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(entity -> new Comment(
                        entity.getId(),
                        entity.getPostId(),
                        entity.getUserId(),
                        entity.getParentId(),
                        entity.getContent(),
                        entity.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return jpaRepository.findById(commentId).map(e -> new Comment(e.getId(), e.getPostId(), e.getUserId(),
                e.getParentId(), e.getContent(), e.getCreatedAt())
        );
    }

    @Override
    public Map<Long, Long> getCounts() {
        List<CommentCount> counts = jpaRepository.getCounts();
        return counts.stream()
                .collect(Collectors.toMap(
                        CommentCount::getPostId,
                        CommentCount::getCount
                ));
    }

    @Override
    public Map<Long, Long> getCountsByPostIds(List<Long> postIds) {
        List<CommentCount> counts = jpaRepository.getCountsByPostIds(postIds);
        return counts.stream()
                .collect(Collectors.toMap(
                        CommentCount::getPostId,
                        CommentCount::getCount
                ));
    }
}