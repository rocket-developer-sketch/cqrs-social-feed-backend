package com.cqrs.socialfeed.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long userId);
    Optional<User> loadByEmail(String email);
    Long save(User user);
}
