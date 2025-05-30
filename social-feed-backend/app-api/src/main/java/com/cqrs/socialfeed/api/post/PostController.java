package com.cqrs.socialfeed.api.post;

import com.cqrs.socialfeed.api.security.CustomUserDetails;
import com.cqrs.socialfeed.command.post.command.CreatePostCommand;
import com.cqrs.socialfeed.command.post.usecase.CreatePostUseCase;
import com.cqrs.socialfeed.domain.post.PostDetailView;
import com.cqrs.socialfeed.query.post.request.GetMyPostsRequest;
import com.cqrs.socialfeed.query.post.response.MyPostResponse;
import com.cqrs.socialfeed.query.post.usecase.PostQueryUseCase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final CreatePostUseCase createPostUseCase;
    private final PostQueryUseCase postQueryUseCase;

    public PostController(CreatePostUseCase createPostUseCase, PostQueryUseCase postQueryUseCase) {
        this.createPostUseCase = createPostUseCase;
        this.postQueryUseCase = postQueryUseCase;
    }

    @GetMapping("/me")
    public ResponseEntity<List<GetMyPostResponse>> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) Long cursorPostId,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        GetMyPostsRequest request = new GetMyPostsRequest(user.getId(), cursorPostId, size);

        return ResponseEntity.ok(postQueryUseCase.getMyPosts(request).stream().map(e ->
                new GetMyPostResponse(e.getPostId(), e.getContent(), e.getCreatedAt(), e.getLikeCount(), e.getCommentCount())).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long id) {
        PostDetailView view = postQueryUseCase.getPostDetail(id);

        return ResponseEntity.ok(new PostDetailResponse(view.getPostId(), view.getAuthorId(), view.getContent(),
                view.getImageUrls(), view.getLikeCount(), view.getCommentCount(), view.getCreatedAt())
        );
    }

    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreatePostRequest request
    ) {
        String locale = LocaleContextHolder.getLocale().getLanguage();

        Long postId = createPostUseCase.create(new CreatePostCommand(
                userDetails.getId(),
                userDetails.getUsername(),
                request.content(),
                request.imageUrls(),
                locale
        ));

        return ResponseEntity.ok(new CreatePostResponse(postId));
    }
}
