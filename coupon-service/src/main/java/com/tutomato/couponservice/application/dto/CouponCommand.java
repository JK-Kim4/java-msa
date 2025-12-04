package com.tutomato.couponservice.application.dto;

import com.tutomato.couponservice.domain.Coupon;
import com.tutomato.couponservice.domain.CouponType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class CouponCommand {

    public static class Create {

        String name;
        double discountValue;
        CouponType couponType;
        int amount;
        LocalDateTime expiredAt;

        protected Create() {
        }

        protected Create(
            String name,
            double discountValue,
            CouponType couponType,
            int amount,
            LocalDateTime expiredAt
        ) {
            this.name = name;
            this.discountValue = discountValue;
            this.couponType = couponType;
            this.amount = amount;
            this.expiredAt = expiredAt;
        }

        public static Create of(String name, double discountValue, CouponType couponType,
            int amount, LocalDateTime expiredAt) {
            return new Create(name, discountValue, couponType, amount, expiredAt);
        }

        public Coupon toEntity() {
            return Coupon.create(
                UUID.randomUUID().toString(),
                this.discountValue,
                this.couponType,
                this.amount,
                this.expiredAt.atZone(ZoneId.of("Asia/Seoul")).toInstant()
            );
        }
    }

    public static class Request {

        String couponId;
        String userId;
        Instant requestedAt;

        protected Request() {
        }

        protected Request(
            String couponId,
            String userId
        ) {
            this.couponId = couponId;
            this.userId = userId;
            this.requestedAt = Instant.now();
        }

        public static Request of(String couponId, String userId) {
            return new Request(couponId, userId);
        }

        public Instant getRequestedAt() {
            return requestedAt;
        }

        public String getCouponId() {
            return couponId;
        }

        public String getUserId() {
            return userId;
        }
    }

    public static class Issue {

        String couponId;
        String userId;
        Instant requestedAt;

        protected Issue() {
        }

        protected Issue(
            String couponId,
            String userId,
            Instant requestedAt
        ) {
            this.couponId = couponId;
            this.userId = userId;
            this.requestedAt = requestedAt;
        }

        public static Issue of(String couponId, String userId, Instant requestedAt) {
            return new Issue(couponId, userId, requestedAt);
        }

        public String getCouponId() {
            return couponId;
        }

        public String getUserId() {
            return userId;
        }

        public Instant getRequestedAt() {
            return requestedAt;
        }
    }
}
