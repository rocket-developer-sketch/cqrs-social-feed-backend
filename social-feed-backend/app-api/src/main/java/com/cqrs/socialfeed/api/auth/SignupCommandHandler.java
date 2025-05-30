package com.cqrs.socialfeed.api.auth;

import com.cqrs.socialfeed.command.auth.command.SignupUserCommand;
import com.cqrs.socialfeed.command.auth.usecase.SignupUserUseCase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SignupCommandHandler {

    private final PasswordEncoder passwordEncoder;
    private final SignupUserUseCase signupUserUseCase;

    public SignupCommandHandler(PasswordEncoder passwordEncoder, SignupUserUseCase signupUserUseCase) {
        this.passwordEncoder = passwordEncoder;
        this.signupUserUseCase = signupUserUseCase;
    }

    public void handle(SignupRequest request) {
        SignupUserCommand command = new SignupUserCommand(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.bio()
        );

        signupUserUseCase.signup(command);
    }
}
