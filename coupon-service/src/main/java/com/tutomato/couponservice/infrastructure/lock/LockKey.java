package com.tutomato.couponservice.infrastructure.lock;

public enum LockKey {

    UPDATE_ITEM_STOCK("update-stock"),
    COUPON_ISSUE("coupon-issue"),;

    private final String code;

    LockKey(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
