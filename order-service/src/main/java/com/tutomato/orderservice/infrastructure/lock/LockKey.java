package com.tutomato.orderservice.infrastructure.lock;

public enum LockKey {

    UPDATE_ITEM_STOCK("update-stock"),
    UPDATE_ORDER("update-order");

    private final String code;

    LockKey(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
