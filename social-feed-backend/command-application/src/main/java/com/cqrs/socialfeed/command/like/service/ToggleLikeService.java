package com.cqrs.socialfeed.command.like.service;

import com.cqrs.socialfeed.command.like.messaging.LikeEventProducer;
import com.cqrs.socialfeed.command.like.response.LikeToggleResponse;
import com.cqrs.socialfeed.command.notification.messaging.NotificationEventProducer;
import com.cqrs.socialfeed.domain.comment.Comment;
import com.cqrs.socialfeed.domain.comment.CommentRepository;
import com.cqrs.socialfeed.domain.like.Like;
import com.cqrs.socialfeed.domain.like.LikeRepository;
import com.cqrs.socialfeed.command.like.command.ToggleLikeCommand;
import com.cqrs.socialfeed.command.like.usecase.ToggleLikeUseCase;
import com.cqrs.socialfeed.domain.like.TargetType;
import com.cqrs.socialfeed.domain.like.messaging.LikeToggledEvent;
import com.cqrs.socialfeed.domain.notification.NotificationTemplate;
import com.cqrs.socialfeed.domain.notification.NotificationTemplateRepository;
import com.cqrs.socialfeed.domain.notification.NotificationType;
import com.cqrs.socialfeed.domain.notification.messaging.NotificationEvent;
import com.cqrs.socialfeed.domain.post.Post;
import com.cqrs.socialfeed.domain.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
public class ToggleLikeService implements ToggleLikeUseCase {
    private final LikeRepository likeRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeEventProducer likeEventProducer;
    private final NotificationEventProducer notificationEventProducer;


    public ToggleLikeService(LikeRepository likeRepository, NotificationTemplateRepository notificationTemplateRepository, PostRepository postRepository, CommentRepository commentRepository,
                             LikeEventProducer likeEventProducer, NotificationEventProducer notificationEventProducer) {
        this.likeRepository = likeRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeEventProducer = likeEventProducer;
        this.notificationEventProducer = notificationEventProducer;
    }

    @Override
    public LikeToggleResponse toogleLike(ToggleLikeCommand command) {
        Long userId = command.getUserId(); // 좋아요 누른 사람
        Long targetId = command.getTargetId();
        TargetType targetType = command.getTargetType();
        boolean isPost = (targetType == TargetType.POST);

        boolean exists = likeRepository.existsByUserIdAndTarget(userId, targetId, targetType);
        boolean liked;

        if (exists) {
            likeRepository.deleteByUserIdAndTarget(userId, targetId, targetType);
            liked = false;

        } else {
            Like like = new Like(null, userId, targetId, targetType, LocalDateTime.now());
            likeRepository.save(like);
            liked = true;
        }

        LikeToggledEvent event = new LikeToggledEvent(
                targetType,
                targetId,
                userId,
                liked,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        likeEventProducer.send(event);

        if (!exists) {
            NotificationEvent notification = null;

            if (isPost) {
                notification = createPostLikeNotification(targetId, command);
            } else {
                notification = createCommentLikeNotification(targetId, command);
            }

            // notification이 생성되었으면 전송
            if (notification != null) {
                notificationEventProducer.send(notification);
            }
        }

        return new LikeToggleResponse(liked);
    }

    // Post에 대한 알림 생성
    private NotificationEvent createPostLikeNotification(Long targetId, ToggleLikeCommand command) {
        Optional<Post> post = postRepository.findById(targetId);

        return post.map(value -> new NotificationEvent(
                NotificationType.POST_LIKED,
                value.getUserId(),
                command.getUserId(),
                generateNotificationMessage(command),
                targetId,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )).orElse(null);
    }

    // Comment에 대한 알림 생성
    private NotificationEvent createCommentLikeNotification(Long targetId, ToggleLikeCommand command) {
        Optional<Comment> comment = commentRepository.findById(targetId);
        // Comment가 존재하지 않으면 null 반환
        return comment.map(value -> new NotificationEvent(
                NotificationType.POST_LIKED,
                value.getUserId(),
                command.getUserId(),
                generateNotificationMessage(command),
                targetId,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )).orElse(null);
    }

    public String generateNotificationMessage(ToggleLikeCommand command) {
        Optional<NotificationTemplate> notificationTemplate;

        if(command.getTargetType() == TargetType.COMMENT) {
            notificationTemplate = notificationTemplateRepository
                .findByCodeAndLocale(NotificationType.COMMENT_LIKED.getTemplateCode(), command.getLocale());
        } else {
            notificationTemplate = notificationTemplateRepository
                    .findByCodeAndLocale(NotificationType.POST_LIKED.getTemplateCode(), command.getLocale());
        }

        Map<String, String> params = new HashMap<>();

        return notificationTemplate
                .map(template -> template.render(params))
                .orElse("A new message has arrived.");
    }

}
