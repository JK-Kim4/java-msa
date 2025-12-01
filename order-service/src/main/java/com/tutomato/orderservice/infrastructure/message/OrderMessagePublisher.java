package com.tutomato.orderservice.infrastructure.message;

import com.tutomato.commonmessaging.order.OrderIssuedMessage;
import org.springframework.kafka.core.KafkaOperations;

public interface OrderMessagePublisher {

    void send(OrderIssuedMessage payload);

    void fail(Object payload);

}
