package com.cqrs.socialfeed.command.comment.command;

public class CreateCommentCommand {
    private final Long postId;
    private final Long userId; // 댓글 작성자의 ID
    private final Long parentId; // nullable
    private final String content;
    private final String locale;

    public CreateCommentCommand(Long postId, Long userId, Long parentId, String content, String locale) {
        this.postId = postId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
        this.locale = locale;
    }

    public Long getPostId() { return postId; }
    public Long getUserId() { return userId; }
    public Long getParentId() { return parentId; }
    public String getContent() { return content; }
    public String getLocale() {
        return locale;
    }
}
