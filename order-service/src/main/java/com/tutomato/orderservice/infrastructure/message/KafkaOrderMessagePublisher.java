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
    public void send(OrderIssuedMessage message) {
        kafkaTemplate.send(KafkaTopics.ORDER_COMPLETED, message);
    }

    @Override
    public void fail(OrderIssuedMessage message) {
        kafkaTemplate.send(KafkaTopics.ORDER_COMPLETED_DLQ, message);
    }
}
