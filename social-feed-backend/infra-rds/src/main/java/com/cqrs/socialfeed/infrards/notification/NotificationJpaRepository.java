package com.cqrs.socialfeed.infrards.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, Long> {
    List<NotificationJpaEntity> findByUserIdAndReadIsFalse(Long userId);
    List<NotificationJpaEntity> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);
    List<NotificationJpaEntity> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);
    List<NotificationJpaEntity> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursorId, Pageable pageable);

    @Modifying
    @Query("UPDATE NotificationJpaEntity n SET n.read  = true WHERE n.userId = :userId AND n.id = :notificationId AND n.read  = false")
    int updateReadTrue(@Param("userId") Long userId, @Param("notificationId") Long notificationId);

    @Modifying
    @Query("UPDATE NotificationJpaEntity n SET n.read = true WHERE n.userId = :userId AND n.id IN :notificationIds AND n.read = false")
    int updateReadsTrue(@Param("userId") Long userId, @Param("notificationIds") List<Long> notificationIds);

    @Modifying
    @Query("UPDATE NotificationJpaEntity n SET n.read  = true WHERE n.userId = :userId AND n.read  = false")
    int updateAllReadTrue(@Param("userId") Long userId);

}