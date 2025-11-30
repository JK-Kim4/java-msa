package com.tutomato.orderservice.infrastructure.message;

import com.tutomato.commonmessaging.order.OrderIssuedMessage;

public interface OrderMessagePublisher {

    void send(OrderIssuedMessage message);

    void fail(OrderIssuedMessage message);

}
