package com.tutomato.paymentservice.infrastructure.message;

import com.tutomato.commonmessaging.payment.PaymentFailMessage;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessagePublisher implements PaymentMessagePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessagePublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CompletableFuture<SendResult<String, Object>> send(PaymentSuccessMessage payload) {
        return kafkaTemplate.send(KafkaTopics.PAYMENT_SUCCESS, payload.paymentId(), payload);
    }

    @Override
    public CompletableFuture<SendResult<String, Object>> fail(PaymentFailMessage payload) {
        return kafkaTemplate.send(KafkaTopics.PAYMENT_FAIL, payload.paymentId(), payload);
    }
}
