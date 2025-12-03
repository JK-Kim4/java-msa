package com.tutomato.couponservice.interfaces.dto;

import com.tutomato.couponservice.application.dto.CouponCommand;
import com.tutomato.couponservice.domain.CouponType;
import java.time.LocalDateTime;

public record CouponCreateRequest(
    String name,
    double discountValue,
    CouponType couponType,
    int amount,
    LocalDateTime expiredAt
) {

    public CouponCommand.Create toCommand() {
        return CouponCommand.Create.of(name, discountValue, couponType, amount, expiredAt);
    }
}
