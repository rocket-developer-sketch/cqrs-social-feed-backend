package com.cqrs.socialfeed.domain.notification;

public enum NotificationType {
    POST_NEW("POST_NEW"),
    COMMENT_NEW("COMMENT_NEW"), //새로운 댓글
    COMMENT_LIKED("COMMENT_LIKED"),
    COMMENT_REPLY("COMMENT_REPLY"), // 댓글의 답글
    POST_LIKED("POST_LIKED"), // 게시글 좋아요
    FOLLOWED_YOU("FOLLOWED_YOU"),
    SYSTEM("SYSTEM_ANNOUNCEMENT ")
    ;

    private final String templateCode;

    NotificationType(String code) {
        this.templateCode = code;
    }

    public String getTemplateCode() {
        return templateCode;
    }
}
