package com.tutomato.couponservice.infrastructure.message;

import com.tutomato.commonmessaging.coupon.CouponCreateMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessagePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessagePublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CouponCreateMessage payload) {
        kafkaTemplate.send(KafkaTopics.COUPON_CREATE, payload);
    }
}
