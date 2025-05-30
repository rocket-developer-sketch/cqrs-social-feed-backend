package com.cqrs.socialfeed.query.feed.request;

public class GetFeedsByUserIdRequest {
    private final Long userId;
    private final Long cursorPostId;
    private final int size;

    public GetFeedsByUserIdRequest(Long userId, Long cursorPostId, int size) {
        this.userId = userId;
        this.cursorPostId = cursorPostId;
        this.size = size;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCursorPostId() {
        return cursorPostId;
    }

    public int getSize() {
        return size;
    }
}
