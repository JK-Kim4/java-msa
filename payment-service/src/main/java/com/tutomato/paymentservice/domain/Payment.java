package com.tutomato.paymentservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "payment_price", nullable = false)
    private Integer paymentPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "paid_at")
    private Instant paidAt;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    protected Payment() {
    }

    protected Payment(
        String paymentId,
        String orderId,
        Integer paymentPrice,
        PaymentStatus paymentStatus
    ) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentPrice = paymentPrice;
        this.paymentStatus = paymentStatus;
        this.createdAt = Instant.now();
    }

    public static Payment create(
        String paymentId,
        String orderId,
        Integer paymentPrice
    ) {
        return new Payment(
            paymentId, orderId, paymentPrice, PaymentStatus.CREATE
        );
    }

    public void paid() {
        this.paidAt = Instant.now();
        this.paymentStatus = PaymentStatus.SUCCESS;
    }

    public Long getId() {
        return id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Integer getPaymentPrice() {
        return paymentPrice;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
