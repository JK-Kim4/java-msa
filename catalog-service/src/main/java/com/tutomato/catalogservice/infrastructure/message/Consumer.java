package com.tutomato.catalogservice.infrastructure.message;

import com.tutomato.commonmessaging.order.OrderIssuedMessage;

public interface Consumer {
    void updateStock(OrderIssuedMessage message);
}
