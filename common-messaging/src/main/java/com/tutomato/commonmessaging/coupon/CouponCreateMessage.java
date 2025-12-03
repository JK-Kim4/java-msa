package com.tutomato.commonmessaging.coupon;

import java.time.Instant;

public record CouponCreateMessage(
    String couponId,
    int amount,
    Instant expiredAt
) {

    public static CouponCreateMessage of(
        String couponId,
        int amount,
        Instant expiredAt
    ) {
        return new CouponCreateMessage(couponId, amount, expiredAt);
    }

}
