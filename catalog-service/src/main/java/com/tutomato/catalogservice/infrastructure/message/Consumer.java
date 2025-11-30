package com.tutomato.catalogservice.infrastructure.message;

public interface Consumer {
    void updateStock(OrderIssuedMessage message);
}
