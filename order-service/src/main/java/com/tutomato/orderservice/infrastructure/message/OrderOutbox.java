package com.tutomato.orderservice.infrastructure.message;

import com.tutomato.commonmessaging.common.AggregateType;
import com.tutomato.commonmessaging.common.OutboxStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "order_outboxs")
public class OrderOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 애그리게잇(도메인)에서 나온 이벤트인지
    @Enumerated(EnumType.STRING)
    @Column(name = "aggregate_type", nullable = false, length = 50)
    private AggregateType aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    // 이벤트 타입 (도메인 이벤트 이름)
    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    // 발행 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OutboxStatus status;  // PENDING, PUBLISHED, FAILED 등

    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    // 타임스탬프
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "published_at")
    private Instant publishedAt;


    protected OrderOutbox() {
    }

    // 팩토리 메서드 등은 필요에 맞게
    public static OrderOutbox pending(
        AggregateType aggregateType,
        String aggregateId,
        String eventType,
        String payload
    ) {
        OrderOutbox outbox = new OrderOutbox();
        outbox.aggregateType = aggregateType;
        outbox.aggregateId = aggregateId;
        outbox.eventType = eventType;
        outbox.payload = payload;
        outbox.status = OutboxStatus.PENDING;
        outbox.createdAt = Instant.now();
        outbox.updatedAt = outbox.createdAt;
        return outbox;
    }

    public void markPublished() {
        this.status = OutboxStatus.PUBLISHED;
        this.publishedAt = Instant.now();
        this.updatedAt = this.publishedAt;
    }

    public void markFailed() {
        this.status = OutboxStatus.FAILED;
        this.retryCount++;
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public AggregateType getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public OutboxStatus getStatus() {
        return status;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }
}
