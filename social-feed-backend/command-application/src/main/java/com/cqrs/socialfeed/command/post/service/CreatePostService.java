package com.cqrs.socialfeed.command.post.service;

import com.cqrs.socialfeed.command.notification.messaging.NotificationEventProducer;
import com.cqrs.socialfeed.command.post.messaging.PostEventProducer;
import com.cqrs.socialfeed.domain.follow.FollowRepository;
import com.cqrs.socialfeed.domain.notification.NotificationTemplate;
import com.cqrs.socialfeed.domain.notification.NotificationTemplateRepository;
import com.cqrs.socialfeed.domain.notification.NotificationType;
import com.cqrs.socialfeed.domain.notification.messaging.NotificationEvent;
import com.cqrs.socialfeed.domain.post.*;
import com.cqrs.socialfeed.command.post.command.CreatePostCommand;
import com.cqrs.socialfeed.command.post.usecase.CreatePostUseCase;
import com.cqrs.socialfeed.domain.post.messaging.PostCreatedEvent;
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
public class CreatePostService implements CreatePostUseCase {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final FollowRepository followRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final PostEventProducer postEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    public CreatePostService(PostRepository postRepository, PostImageRepository postImageRepository,
                             FollowRepository followRepository, NotificationTemplateRepository notificationTemplateRepository,
                             PostEventProducer postEventProducer, NotificationEventProducer notificationEventProducer) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        this.followRepository = followRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.postEventProducer = postEventProducer;
        this.notificationEventProducer = notificationEventProducer;
    }

    @Override
    public Long create(CreatePostCommand command) {
        LocalDateTime now = LocalDateTime.now();

        Post post = new Post(null, command.getUserId(), command.getUsername(), command.getContent(), now, now);
        Long postId = postRepository.save(post); // 저장 후 ID 반환

        int order = 0;
        for (String imageUrl : command.getImageUrls()) {
            PostImage image = new PostImage(null, postId, imageUrl, order++);
            postImageRepository.save(image);
        }

        List<Long> followeeIds = followRepository.findFolloweeIdsByFollowerId(command.getUserId());

        if(!followeeIds.isEmpty()) {

            for (Long receiverId : followeeIds) {
                // Kafka 이벤트 발행
                postEventProducer.send(new PostCreatedEvent(
                        postId,
                        command.getUserId(),
                        command.getContent(),
                        now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        receiverId)
                );

                notificationEventProducer.send(new NotificationEvent(NotificationType.POST_NEW,
                        receiverId, command.getUserId(),
                        generateNotificationMessage(command),
                        postId, now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                );
            }
        }

        return postId;
    }

    public String generateNotificationMessage(CreatePostCommand command) {
        Optional<NotificationTemplate> notificationTemplate = notificationTemplateRepository
                .findByCodeAndLocale(NotificationType.POST_NEW.getTemplateCode(), command.getLocale());

        Map<String, String> params = new HashMap<>();
        params.put("author", command.getUsername());

        return notificationTemplate
                .map(template -> template.render(params))
                .orElse("A new message has arrived.");
    }
}
