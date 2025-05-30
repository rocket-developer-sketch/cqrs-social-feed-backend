package com.cqrs.socialfeed.infrards.user;

import com.cqrs.socialfeed.domain.user.User;
import com.cqrs.socialfeed.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(entity -> new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getProfileImageUrl(),
                entity.getBio(),
                entity.getCreatedAt()
        ));
    }

    @Override
    public Optional<User> findById(Long userId) {
        return jpaRepository.findById(userId).map(entity -> new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getProfileImageUrl(),
                entity.getBio(),
                entity.getCreatedAt()
        ));
    }

    @Override
    public Optional<User> loadByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(entity -> new User(
                        entity.getId(),
                        entity.getUsername(),
                        entity.getEmail(),
                        entity.getPasswordHash(),
                        entity.getProfileImageUrl(),
                        entity.getBio(),
                        entity.getCreatedAt()
                ));
    }

    @Override
    public Long save(User user) {
        UserJpaEntity entity = new UserJpaEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getProfileImageUrl(),
                user.getBio(),
                user.getCreatedAt()
        );

        return jpaRepository.save(entity).getId();
    }
}
