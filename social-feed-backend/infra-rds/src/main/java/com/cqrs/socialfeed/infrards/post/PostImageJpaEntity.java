package com.cqrs.socialfeed.infrards.post;


import jakarta.persistence.*;

@Entity
@Table(name = "post_images")
public class PostImageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer displayOrder;

    protected PostImageJpaEntity() {
    }

    public PostImageJpaEntity(Long id, Long postId, String imageUrl, Integer displayOrder) {
        this.id = id;
        this.postId = postId;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}
