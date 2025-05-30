package com.cqrs.socialfeed.api.comment;

import com.cqrs.socialfeed.api.security.CustomUserDetails;
import com.cqrs.socialfeed.command.comment.command.CreateCommentCommand;
import com.cqrs.socialfeed.command.comment.response.CommentCreateResponse;
import com.cqrs.socialfeed.command.comment.usecase.CreateCommentUseCase;
import com.cqrs.socialfeed.query.comment.request.GetCommentsByPostIdRequest;

import com.cqrs.socialfeed.query.comment.usecase.CommentQueryUseCase;
import com.cqrs.socialfeed.domain.comment.Comment;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CreateCommentUseCase createCommentUseCase;
    private final CommentQueryUseCase readCommentUseCase;

    public CommentController(CreateCommentUseCase createCommentUseCase,
                             CommentQueryUseCase readCommentUseCase) {
        this.createCommentUseCase = createCommentUseCase;
        this.readCommentUseCase = readCommentUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateCommentResponse> createComment(@RequestBody CreateCommentRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        String locale = LocaleContextHolder.getLocale().getLanguage();
        CreateCommentCommand command = new CreateCommentCommand(request.postId(), user.getId(), request.parentId(), request.content(), locale);
        CommentCreateResponse comment = createCommentUseCase.create(command, locale);

        return ResponseEntity.ok(new CreateCommentResponse(comment.getCommentId()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<GetPostCommentResponse>> getComments(@PathVariable Long postId) {
        List<Comment> comments = readCommentUseCase.getCommentsByPostId(new GetCommentsByPostIdRequest(postId));

        return ResponseEntity.ok(comments.stream().map(e -> new GetPostCommentResponse(e.getId(), e.getPostId(), e.getUserId(), e.getParentId(),
                e.getContent(), e.getCreatedAt())).toList());
    }
}
