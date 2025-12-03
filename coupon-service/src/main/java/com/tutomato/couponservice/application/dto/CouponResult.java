package com.tutomato.couponservice.application.dto;

import com.tutomato.couponservice.domain.Coupon;
import com.tutomato.couponservice.domain.CouponType;
import java.time.Instant;

public class CouponResult {

    public static class Create {

        String couponId;
        double discountValue;
        CouponType couponType;
        int totalAmount;
        Instant expiredAt;

        protected Create(
            String couponId, double discountValue, CouponType couponType, int totalAmount,
            Instant expiredAt) {
            this.couponId = couponId;
            this.discountValue = discountValue;
            this.couponType = couponType;
            this.totalAmount = totalAmount;
            this.expiredAt = expiredAt;
        }

        public static Create from(Coupon coupon) {
            return new Create(
                coupon.getCouponId(),
                coupon.getDiscountValue(),
                coupon.getCouponType(),
                coupon.getTotalAmount(),
                coupon.getExpiredAt()
            );
        }

        public String getCouponId() {
            return couponId;
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

}
