package com.cqrs.socialfeed.domain.common;

import java.time.LocalDateTime;


/**
 * 생성/수정 시간 정보를 포함한 공통 추상 클래스
 */
public class BaseTimeEntity {
    protected final LocalDateTime createdAt;
    protected final LocalDateTime updatedAt;

    protected BaseTimeEntity(LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
