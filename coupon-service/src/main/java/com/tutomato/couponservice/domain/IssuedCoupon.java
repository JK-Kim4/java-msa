package com.tutomato.couponservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "issued_coupons")
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_seq")
    private Coupon coupon;

    @Column(name = "own_user_id")
    private String ownUserId;

    @Column(name = "requested_at")
    private Instant requestedAt;

    @Column(name = "is_used")
    private boolean isUsed;


    protected IssuedCoupon() {
    }

    protected IssuedCoupon(Coupon coupon, String userId, Instant requestedAt, boolean isUsed) {
        this.coupon = coupon;
        this.ownUserId = userId;
        this.requestedAt = requestedAt;
        this.isUsed = isUsed;
    }

    public static IssuedCoupon issue(
        Coupon coupon,
        String userId,
        Instant requestedAt
    ) {
        return new IssuedCoupon(coupon, userId, requestedAt, false);
    }

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public String getOwnUserId() {
        return ownUserId;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
