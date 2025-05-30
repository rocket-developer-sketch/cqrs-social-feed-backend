package com.cqrs.socialfeed.command.auth.usecase;

import com.cqrs.socialfeed.command.auth.command.SignupUserCommand;

public interface SignupUserUseCase {
    void signup(SignupUserCommand command);
}
