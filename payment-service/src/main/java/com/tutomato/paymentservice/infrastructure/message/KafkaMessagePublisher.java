package com.tutomato.paymentservice.infrastructure.message;

import com.tutomato.commonmessaging.payment.PaymentFailMessage;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessagePublisher implements PaymentMessagePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessagePublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(PaymentSuccessMessage payload) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_SUCCESS, payload.paymentId(), payload);
    }

    @Override
    public void fail(PaymentFailMessage payload) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_FAIL, payload.paymentId(), payload);
    }
}
