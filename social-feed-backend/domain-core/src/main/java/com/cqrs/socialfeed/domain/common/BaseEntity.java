package com.cqrs.socialfeed.domain.common;

import java.time.LocalDateTime;

/**
 * ID + 시간 필드를 가진 공통 추상 엔티티
 */
public abstract class BaseEntity<ID> extends BaseTimeEntity {
    protected final ID id;

    protected BaseEntity(ID id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
    }

    public ID getId() { return id; }
}