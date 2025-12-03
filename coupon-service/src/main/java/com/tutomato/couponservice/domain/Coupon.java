package com.tutomato.couponservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id")
    private String couponId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "discount_value")
    private double discountValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type")
    private CouponType couponType;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "expired_at")
    private Instant expiredAt;

    protected Coupon() {
    }

    protected Coupon(
        String couponId,
        double discountValue,
        CouponType couponType,
        int totalAmount,
        Instant expiredAt,
        Boolean isActive
    ) {
        this.couponId = couponId;
        this.discountValue = discountValue;
        this.couponType = couponType;
        this.totalAmount = totalAmount;
        this.expiredAt = expiredAt;
        this.isActive = isActive;
    }

    public static Coupon create(
        String couponId,
        double discountValue,
        CouponType couponType,
        int totalAmount,
        Instant expiredAt
    ) {
        return new Coupon(couponId, discountValue, couponType, totalAmount, expiredAt, false);
    }

    public boolean isIssuable(int issuedAmount, Instant requestedAt) {
        if (this.totalAmount == 0) {
            return true;
        }

        return (issuedAmount < this.totalAmount && requestedAt.isBefore(this.expiredAt));
    }

    public void active() {
        this.isActive = true;
    }

    public void inactive() {
        this.isActive = false;
    }

    public Long getId() {
        return id;
    }

    public String getCouponId() {
        return couponId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }
}
