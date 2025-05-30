package com.cqrs.socialfeed.command.like.messaging;

import com.cqrs.socialfeed.domain.like.messaging.LikeToggledEvent;

public interface LikeEventProducer {
    void send(LikeToggledEvent event);
}
