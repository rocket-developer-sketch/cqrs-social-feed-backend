package com.cqrs.socialfeed.api.like;

import com.cqrs.socialfeed.api.security.CustomUserDetails;
import com.cqrs.socialfeed.command.like.command.ToggleLikeCommand;
import com.cqrs.socialfeed.command.like.usecase.ToggleLikeUseCase;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final ToggleLikeUseCase toggleLikeUseCase;

    public LikeController(ToggleLikeUseCase toggleLikeUseCase) {
        this.toggleLikeUseCase = toggleLikeUseCase;
    }

    @PostMapping("/toggle")
    public ResponseEntity<ToggleLikeResponse> toggleLike(@RequestBody ToggleLikeRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        String locale = LocaleContextHolder.getLocale().getLanguage();
        ToggleLikeCommand command = new ToggleLikeCommand(user.getId(), request.targetId(), request.targetType(), locale);

        return ResponseEntity.ok(new ToggleLikeResponse(toggleLikeUseCase.toogleLike(command).isLiked()));
    }
}
