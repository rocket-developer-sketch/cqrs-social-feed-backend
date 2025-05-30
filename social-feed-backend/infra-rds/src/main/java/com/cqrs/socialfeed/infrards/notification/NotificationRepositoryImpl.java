package com.cqrs.socialfeed.infrards.notification;

import com.cqrs.socialfeed.domain.notification.Notification;
import com.cqrs.socialfeed.domain.notification.NotificationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;

    public NotificationRepositoryImpl(NotificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Long save(Notification notification) {
        NotificationJpaEntity entity = new NotificationJpaEntity(
                notification.getId(),
                notification.getUserId(),
                notification.getContent(),
                notification.getNotificationType(),
                notification.isRead(),
                notification.getCreatedAt()
        );
        return jpaRepository.save(entity).getId();
    }

    @Override
    public List<Notification> findUnreadByUserId(Long userId) {
        return jpaRepository.findByUserIdAndReadIsFalse(userId).stream()
                .map(e -> new Notification(
                        e.getId(), e.getUserId(), e.getContent(), e.getType(), e.isRead(), e.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findRecentUnreadByUserId(Long userId) {
        Pageable pageable = (Pageable) PageRequest.of(0, 20);
        return jpaRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId, pageable)
                .stream()
                .map(e -> new Notification(
                        e.getId(), e.getUserId(), e.getContent(), e.getType(), e.isRead(), e.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByUserIdAndCursor(Long userId, Long cursorId, int size) {
        Pageable pageable = (Pageable) PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        List<NotificationJpaEntity> entities = (cursorId == null)
                ? jpaRepository.findByUserIdOrderByIdDesc(userId, pageable)
                : jpaRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursorId, pageable);

        return entities.stream()
                .map(e -> new Notification(
                        e.getId(), e.getUserId(), e.getContent(), e.getType(), e.isRead(), e.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public int markAsReads(Long userId, List<Long> notificationId) {
        return jpaRepository.updateReadsTrue(userId, notificationId);
    }

    @Override
    public int markAllAsReadByUserId(Long userId) {
        return jpaRepository.updateAllReadTrue(userId);
    }

    @Override
    public void saveAll(List<Notification> notifications) {
        List<NotificationJpaEntity> notificationJpaEntities = notifications.stream()
                .map(e -> new NotificationJpaEntity(e.getId(), e.getUserId(), e.getContent(), e.getNotificationType(), e.isRead(), e.getCreatedAt()))
                .toList();
        jpaRepository.saveAll(notificationJpaEntities);
    }
}
