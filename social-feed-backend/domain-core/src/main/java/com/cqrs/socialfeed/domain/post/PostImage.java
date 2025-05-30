package com.cqrs.socialfeed.domain.post;

public class PostImage {
    private final Long id;
    private final Long postId;
    private final String imageUrl;
    private final Integer order;

    public PostImage(Long id, Long postId, String imageUrl, Integer order) {
        this.id = id;
        this.postId = postId;
        this.imageUrl = imageUrl;
        this.order = order;
    }

    public Long getId() { return id; }

    public Long getPostId() { return postId; }

    public String getImageUrl() { return imageUrl; }

    public Integer getOrder() { return order; }
}
