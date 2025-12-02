package com.tutomato.orderservice.infrastructure.message;

public interface OrderMessagePublisher {

    void send(String topic, String partitionKey, Object payload);

    void fail(Object payload);
}
