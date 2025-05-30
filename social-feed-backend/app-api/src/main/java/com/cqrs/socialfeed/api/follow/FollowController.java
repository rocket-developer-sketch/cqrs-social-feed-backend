package com.cqrs.socialfeed.api.follow;

import com.cqrs.socialfeed.api.security.CustomUserDetails;
import com.cqrs.socialfeed.command.follow.command.FollowUserCommand;
import com.cqrs.socialfeed.command.follow.command.UnfollowUserCommand;
import com.cqrs.socialfeed.command.follow.usercase.FollowUserUseCase;
import com.cqrs.socialfeed.command.follow.usercase.UnfollowUserUseCase;
import com.cqrs.socialfeed.query.follow.usecase.FollowQueryUseCase;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follows")
public class FollowController {

    private final FollowUserUseCase followUseCase;
    private final UnfollowUserUseCase unfollowUseCase;
    private final FollowQueryUseCase followQueryUseCase;

    public FollowController(FollowUserUseCase followUseCase, UnfollowUserUseCase unfollowUseCase, FollowQueryUseCase followQueryUseCase) {
        this.followUseCase = followUseCase;
        this.unfollowUseCase = unfollowUseCase;
        this.followQueryUseCase = followQueryUseCase;
    }

    // 팔로우 요청
    @PostMapping("/{targetUserId}")
    public ResponseEntity<Void> follow(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long targetUserId
    ) {
        String locale = LocaleContextHolder.getLocale().getLanguage();
        followUseCase.follow(new FollowUserCommand(targetUserId, user.getId(), locale));
        return ResponseEntity.noContent().build();
    }

    // 언팔로우 요청
    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<Void> unfollow(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long targetUserId
    ) {
        unfollowUseCase.unfollow(new UnfollowUserCommand(user.getId(), targetUserId));
        return ResponseEntity.noContent().build();
    }

    // 내가 팔로우한 목록
    @GetMapping("/me/following")
    public ResponseEntity<List<MyFollowingResponse>> getFollowingList(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(followQueryUseCase.getFollowing(user.getId()).stream()
                .map(e -> new MyFollowingResponse(e.getId(), e.getUsername(), e.getProfileImageUrl())).toList());
    }

    // 나를 팔로우한 목록
    @GetMapping("/me/followers")
    public ResponseEntity<List<MyFollowerResponse>> getFollowersList(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(followQueryUseCase.getFollowers(user.getId()).stream().map(e ->
                new MyFollowerResponse(e.getId(), e.getUsername(), e.getProfileImageUrl())).toList());
    }

    // 특정 유저 팔로우 여부
    @GetMapping("/{userId}/is-following")
    public ResponseEntity<IsFollowingResponse> isFollowing(@AuthenticationPrincipal CustomUserDetails user,
                                                           @PathVariable Long userId) {
        boolean isFollowing = followQueryUseCase.isFollowing(user.getId(), userId);
        return ResponseEntity.ok(new IsFollowingResponse(isFollowing));
    }
}
