package com.cqrs.socialfeed.command.auth.usecase;

import com.cqrs.socialfeed.command.auth.command.LoginTokenCommand;
import com.cqrs.socialfeed.command.auth.command.TokenPair;
import com.cqrs.socialfeed.domain.user.User;

public interface TokenProviderUseCase {
    String generateToken(User user);
    String getUsernameFromToken(String token);
    String createRefreshToken(String username);
    void deleteTokenByUser(Long userId);
    TokenPair handleRefreshToken(String refreshToken);
    TokenPair handleToken(LoginTokenCommand command);
}
