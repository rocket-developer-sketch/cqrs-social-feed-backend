package com.cqrs.socialfeed.api.feed;

import com.cqrs.socialfeed.api.security.CustomUserDetails;
import com.cqrs.socialfeed.query.feed.request.GetFeedsByUserIdRequest;
import com.cqrs.socialfeed.query.feed.response.FeedResponse;
import com.cqrs.socialfeed.query.feed.usecase.FeedQueryUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedController {

    private final FeedQueryUseCase feedQueryUseCase;

    public FeedController(FeedQueryUseCase feedQueryUseCase) {
        this.feedQueryUseCase = feedQueryUseCase;
    }

    @GetMapping
    public ResponseEntity<List<GetMyFeed>> getFeeds(@AuthenticationPrincipal CustomUserDetails user,
                                               @RequestParam(required = false) Long cursorPostId,
                                               @RequestParam(defaultValue = "10") int size) {
        GetFeedsByUserIdRequest request = new GetFeedsByUserIdRequest(user.getId(), cursorPostId, size);
        List<FeedResponse> feeds = feedQueryUseCase.getFeeds(request);

        return ResponseEntity.ok(feeds.stream().map(e -> new GetMyFeed(e.getPostId(), e.getPostAuthorId(), e.getCreatedAt(),
                e.getPushedAt(), e.getContent(), e.getLikeCount(), e.getCommentCount())).toList());
    }
}
