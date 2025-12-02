package com.tutomato.catalogservice.infrastructure.message;

import com.tutomato.commonmessaging.order.OrderIssuedMessage;
import com.tutomato.commonmessaging.order.OrderPendingMessage;

public interface Consumer {

    void consume(OrderPendingMessage message);
}
