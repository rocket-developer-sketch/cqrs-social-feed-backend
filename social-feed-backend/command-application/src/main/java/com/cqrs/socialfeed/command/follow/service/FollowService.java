package com.cqrs.socialfeed.command.follow.service;

import com.cqrs.socialfeed.command.follow.command.FollowUserCommand;
import com.cqrs.socialfeed.command.follow.command.UnfollowUserCommand;
import com.cqrs.socialfeed.command.follow.usercase.FollowUserUseCase;
import com.cqrs.socialfeed.command.follow.usercase.UnfollowUserUseCase;
import com.cqrs.socialfeed.command.notification.messaging.NotificationEventProducer;
import com.cqrs.socialfeed.domain.follow.Follow;
import com.cqrs.socialfeed.domain.follow.FollowRepository;
import com.cqrs.socialfeed.domain.notification.NotificationTemplate;
import com.cqrs.socialfeed.domain.notification.NotificationTemplateRepository;
import com.cqrs.socialfeed.domain.notification.NotificationType;
import com.cqrs.socialfeed.domain.notification.messaging.NotificationEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
public class FollowService implements FollowUserUseCase, UnfollowUserUseCase {

    private final FollowRepository followRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationEventProducer notificationEventProducer;

    public FollowService(FollowRepository followRepository, NotificationTemplateRepository notificationTemplateRepository, NotificationEventProducer notificationEventProducer) {
        this.followRepository = followRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.notificationEventProducer = notificationEventProducer;
    }

    @Override
    public void follow(FollowUserCommand command) {
        if (command.getFolloweeId().equals(command.getFollowerId())) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        if (followRepository.isFollowing(command.getFollowerId(), command.getFolloweeId())) {
            return; // 이미 팔로우한 경우 무시
        }

        Follow follow = new Follow(
                null,
                command.getFollowerId(),
                command.getFolloweeId(),
                LocalDateTime.now()
        );

        followRepository.follow(follow);

        NotificationEvent notification = new NotificationEvent(NotificationType.POST_NEW, List.of(command.getFollowerId()), command.getFolloweeId(),
                generateNotificationMessage(command), command.getFollowerId(), LocalDateTime.now().toString());

        notificationEventProducer.send(notification);
    }

    @Override
    public void unfollow(UnfollowUserCommand command) {
        Follow follow = new Follow(
                null,
                command.getFollowerId(),
                command.getFolloweeId(),
                LocalDateTime.now()
        );


        followRepository.unfollow(follow);
    }

    public String generateNotificationMessage(FollowUserCommand command) {
        Optional<NotificationTemplate> notificationTemplate = notificationTemplateRepository
                .findByCodeAndLocale(NotificationType.FOLLOWED_YOU.getTemplateCode(), command.getLocale());

        Map<String, String> params = new HashMap<>();

        return notificationTemplate
                .map(template -> template.render(params))
                .orElse("A new message has arrived.");
    }

}
