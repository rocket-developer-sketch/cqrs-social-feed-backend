package com.cqrs.socialfeed.api.notification;

import com.cqrs.socialfeed.api.security.CustomUserDetails;
import com.cqrs.socialfeed.query.notification.request.GetNotificationRequest;
import com.cqrs.socialfeed.command.notification.command.MarkAllNotificationsAsReadCommand;
import com.cqrs.socialfeed.command.notification.command.MarkNotificationAsReadCommand;
import com.cqrs.socialfeed.query.notification.response.NotificationListResponse;
import com.cqrs.socialfeed.query.notification.response.NotificationResponse;
import com.cqrs.socialfeed.query.notification.usecase.NotificationQueryUseCase;
import com.cqrs.socialfeed.command.notification.usecase.ReadNotificationUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationQueryUseCase notificationUseCase;
    private final ReadNotificationUseCase readNotificationUseCase;

    public NotificationController(NotificationQueryUseCase notificationUseCase, ReadNotificationUseCase readNotificationUseCase) {
        this.notificationUseCase = notificationUseCase;
        this.readNotificationUseCase = readNotificationUseCase;
    }

    @GetMapping
    public ResponseEntity<GetNotificationListResponse> getNotifications (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "20") int size) {
        NotificationListResponse notificationListResponse = notificationUseCase.getNotifications(new GetNotificationRequest(userDetails.getId(), cursorId, size));
        readNotificationUseCase.markMultipleAsRead(new MarkNotificationAsReadCommand(userDetails.getId(),
                notificationListResponse.getNotifications().stream().map(NotificationResponse::getId).toList()));

        return ResponseEntity.ok(new GetNotificationListResponse(notificationListResponse.getNotifications(), notificationListResponse.isHasNext()));
    }

    @PatchMapping("/read")
    public ResponseEntity<Void> markMultipleAsRead(@RequestBody List<Long> notificationIds, @AuthenticationPrincipal CustomUserDetails userDetails) {
        readNotificationUseCase.markMultipleAsRead(new MarkNotificationAsReadCommand(userDetails.getId(), notificationIds));
        return ResponseEntity.ok().build();
    }

    /**
     * 클라이언트 상단 뱃지에 표시할 "읽지 않은 알림 개수" 반환
     *
     * 빠르게 응답해야 하므로 Redis에서 가져옴
     * */
    @GetMapping("/unread-count")
    public ResponseEntity<CountUnreadNotificationResponse> getUnreadCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(new CountUnreadNotificationResponse(notificationUseCase.getUnreadCount(userDetails.getId())));
    }

    /**
     * 사용자가 “모든 알림을 읽음 처리” 할 수 있게 함
     *
     * DB의 isRead = true로 일괄 업데이트
     *
     * Redis 캐시된 안 읽은 수 → 0으로 덮어씀
     *
     * */
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal CustomUserDetails userDetails) {
        readNotificationUseCase.markAllAsRead(new MarkAllNotificationsAsReadCommand(userDetails.getId()));
        return ResponseEntity.ok().build();
    }

}
