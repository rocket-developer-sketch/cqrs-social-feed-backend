package com.cqrs.socialfeed.command.like.response;

public class LikeToggleResponse {
    private final boolean liked;

    public LikeToggleResponse(boolean liked/*, long likeCount*/) {
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }
}
