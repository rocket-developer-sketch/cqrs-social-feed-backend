package com.cqrs.socialfeed.command.notification.usecase;

import com.cqrs.socialfeed.command.notification.command.NotifyUserCommand;

public interface NotifyUseCase {
    void notify(NotifyUserCommand command);
}
