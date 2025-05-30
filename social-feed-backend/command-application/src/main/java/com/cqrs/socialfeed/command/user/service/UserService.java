package com.cqrs.socialfeed.command.user.service;

import com.cqrs.socialfeed.command.user.command.UpdateProfileImageCommand;
import com.cqrs.socialfeed.command.user.usecase.UpdateProfileImageUseCase;
import com.cqrs.socialfeed.domain.user.User;
import com.cqrs.socialfeed.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService implements UpdateProfileImageUseCase {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void updateProfileImage(UpdateProfileImageCommand command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.updateProfileImage(command.getImageUrl());

        userRepository.save(user);
    }
}
