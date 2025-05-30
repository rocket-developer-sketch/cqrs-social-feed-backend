package com.cqrs.socialfeed.command.comment.service;


import com.cqrs.socialfeed.command.comment.command.CreateCommentCommand;
import com.cqrs.socialfeed.command.comment.messaging.CommentEventProducer;
import com.cqrs.socialfeed.command.comment.response.CommentCreateResponse;
import com.cqrs.socialfeed.command.comment.usecase.CreateCommentUseCase;
import com.cqrs.socialfeed.command.notification.messaging.NotificationEventProducer;
import com.cqrs.socialfeed.domain.comment.Comment;
import com.cqrs.socialfeed.domain.comment.CommentRepository;
import com.cqrs.socialfeed.domain.comment.messaging.CommentCreatedEvent;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
public class CreateCommentService implements CreateCommentUseCase {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final CommentEventProducer commentEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    public CreateCommentService(CommentRepository commentRepository,
                                PostRepository postRepository, NotificationTemplateRepository notificationTemplateRepository,
                                CommentEventProducer commentEventProducer, NotificationEventProducer notificationEventProducer) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.commentEventProducer = commentEventProducer;
        this.notificationEventProducer = notificationEventProducer;
    }
    @Override
    public CommentCreateResponse create(CreateCommentCommand command, String locale) {
        Comment comment = new Comment(
                null,
                command.getPostId(),
                command.getUserId(),
                command.getParentId(),
                command.getContent(),
                LocalDateTime.now()
        );
        Long commentId = commentRepository.save(comment);

        Long targetUserId = resolveTargetUserId(command);

        if (!command.getUserId().equals(targetUserId)) {
            // Kafka 이벤트 발행
            commentEventProducer.send(new CommentCreatedEvent(
                    command.getPostId(),
                    targetUserId,
                    command.getContent(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            ));

            notificationEventProducer.send(new NotificationEvent(
                    NotificationType.COMMENT_NEW, targetUserId,
                    command.getUserId(), generateNotificationMessage(command),
                    command.getPostId(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        }

        return new CommentCreateResponse(commentId);
    }

    private Long resolveTargetUserId(CreateCommentCommand command) {
        if (command.getParentId() != null && command.getParentId() > 0) {
            Comment parent = commentRepository.findById(command.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글이 존재하지 않습니다."));
            return parent.getUserId();
        } else {
            Post post = postRepository.findById(command.getPostId())
                    .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
            return post.getUserId();
        }
    }


    public String generateNotificationMessage(CreateCommentCommand command) {
        Optional<NotificationTemplate> notificationTemplate = notificationTemplateRepository
                .findByCodeAndLocale(NotificationType.COMMENT_NEW.getTemplateCode(), command.getLocale());

        Map<String, String> params = new HashMap<>();

        return notificationTemplate
                .map(template -> template.render(params))
                .orElse("A new message has arrived.");
    }
}
