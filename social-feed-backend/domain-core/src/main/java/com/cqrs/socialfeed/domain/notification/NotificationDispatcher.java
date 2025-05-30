package com.cqrs.socialfeed.domain.notification;

public interface NotificationDispatcher {
    void dispatch(Notification notification);
}
