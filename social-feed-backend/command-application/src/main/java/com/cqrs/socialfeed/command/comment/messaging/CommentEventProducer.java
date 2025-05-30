package com.cqrs.socialfeed.command.comment.messaging;

import com.cqrs.socialfeed.domain.comment.messaging.CommentCreatedEvent;

public interface CommentEventProducer {
    void send(CommentCreatedEvent event);
}
