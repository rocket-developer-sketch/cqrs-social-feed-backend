package com.cqrs.socialfeed.command.user.usecase;

import com.cqrs.socialfeed.command.user.command.UpdateProfileImageCommand;

public interface UpdateProfileImageUseCase {
    void updateProfileImage(UpdateProfileImageCommand command);
}
