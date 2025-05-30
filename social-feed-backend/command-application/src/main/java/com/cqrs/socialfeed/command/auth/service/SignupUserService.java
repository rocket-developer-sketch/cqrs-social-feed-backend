package com.cqrs.socialfeed.command.auth.service;

import com.cqrs.socialfeed.command.auth.command.SignupUserCommand;
import com.cqrs.socialfeed.command.auth.usecase.SignupUserUseCase;
import com.cqrs.socialfeed.domain.user.User;
import com.cqrs.socialfeed.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
public class SignupUserService implements SignupUserUseCase {
    private final UserRepository userRepository;

    public SignupUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signup(SignupUserCommand command) {
        if (userRepository.loadByEmail(command.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        User user = new User(
                null,
                command.getUsername(),
                command.getEmail(),
                command.getPassword(),
                null,
                command.getBio(),
                LocalDateTime.now()
        );

        userRepository.save(user);
    }
}
