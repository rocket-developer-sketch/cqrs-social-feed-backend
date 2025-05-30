package com.cqrs.socialfeed.command.post.messaging;

import com.cqrs.socialfeed.domain.post.messaging.PostCreatedEvent;

public interface PostEventProducer {
    void send(PostCreatedEvent event);
}
