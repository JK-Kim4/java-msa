package com.tutomato.catalogservice.infrastructure.message;

import com.tutomato.commonmessaging.catalog.DecreaseStockCompleteMessage;
import com.tutomato.commonmessaging.catalog.DecreaseStockFailMessage;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessagePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessagePublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(DecreaseStockCompleteMessage payload) {
        kafkaTemplate.send(KafkaTopics.CATALOG_STOCK_DECREASE, payload.orderId(), payload);
    }

    public void fail(DecreaseStockFailMessage payload) {
        kafkaTemplate.send(KafkaTopics.CATALOG_STOCK_DECREASE_FAIL, payload.orderId(), payload);
    }
}
