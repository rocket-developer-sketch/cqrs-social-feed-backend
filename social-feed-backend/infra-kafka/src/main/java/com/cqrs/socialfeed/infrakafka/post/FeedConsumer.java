package com.cqrs.socialfeed.infrakafka.post;

import com.cqrs.socialfeed.domain.feed.Feed;
import com.cqrs.socialfeed.domain.feed.FeedRepository;
import com.cqrs.socialfeed.domain.post.messaging.PostCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FeedConsumer {
    private static final Logger log = LoggerFactory.getLogger(FeedConsumer.class);

    private final ObjectMapper objectMapper;
    private final FeedRepository feedRepository;

    public FeedConsumer(ObjectMapper objectMapper, FeedRepository feedRepository) {
        this.objectMapper = objectMapper;
        this.feedRepository = feedRepository;
    }

    @KafkaListener(topics = "post.created", groupId = "feed-consumer-group")
    public void consume(String message) {
        try {
            PostCreatedEvent event = objectMapper.readValue(message, PostCreatedEvent.class);
            Long followerId = event.getFollowerId();


                Feed feed = new Feed(
                        followerId,
                        event.getPostId(),
                        event.getAuthorId(),
                        LocalDateTime.parse(event.getCreatedAt()),              // createdAt (post 작성 시간)
                        LocalDateTime.now()             // pushedAt (피드에 들어온 시간)
                );

                feedRepository.save(feed);

            log.info("[FeedConsumer] Distributed post {} to {} follower", event.getPostId(), event.getFollowerId());

        } catch (Exception e) {
            log.error("[FeedConsumer] Failed to consume feed.distribute: {}", e.getMessage(), e);
        }
    }
}
