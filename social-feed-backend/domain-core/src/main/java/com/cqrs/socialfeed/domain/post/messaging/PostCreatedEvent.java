package com.cqrs.socialfeed.domain.post.messaging;

public class PostCreatedEvent {
    private Long postId;
    private Long authorId;
    private String content;
    private String createdAt;
    //private List<Long> followerIds;
    private Long followerId;

    protected PostCreatedEvent(){}

    public PostCreatedEvent(Long postId, Long authorId, String content, String createdAt,  Long followerId) {
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt;
        //this.followerIds = followerIds;
        this.followerId = followerId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

//    public List<Long> getFollowerIds() {
//        return followerIds;
//    }

    public Long getFollowerId() {
        return followerId;
    }
}
