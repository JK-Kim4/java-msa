package com.tutomato.catalogservice.infrastructure.lock;

public enum LockKey {

    UPDATE_ITEM_STOCK("update-stock");

    private final String code;

    LockKey(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
