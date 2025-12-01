package com.tutomato.orderservice.infrastructure.message;

import com.tutomato.commonmessaging.order.OrderIssuedMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderMessagePublisher implements OrderMessagePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaOrderMessagePublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(OrderIssuedMessage payload) {
        kafkaTemplate.send(KafkaTopics.ORDER_COMPLETED, payload.orderId(), payload);
    }

    @Override
    public void fail(Object payload) {
        kafkaTemplate.send(KafkaTopics.ORDER_COMPLETED_DLQ, payload);
    }
}
