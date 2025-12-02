package com.tutomato.paymentservice.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.commonmessaging.order.OrderPendingMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import com.tutomato.commonmessaging.topic.KafkaTopics.TopicGroups;
import com.tutomato.paymentservice.application.OrderPaymentService;
import com.tutomato.paymentservice.application.PaymentCommand;
import com.tutomato.paymentservice.domain.Payment;
import com.tutomato.paymentservice.infrastructure.lock.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final RedisRepository redisRepository;
    private final OrderPaymentService orderPaymentService;

    public KafkaConsumer(
        RedisRepository redisRepository,
        OrderPaymentService orderPaymentService
    ) {
        this.redisRepository = redisRepository;
        this.orderPaymentService = orderPaymentService;
    }

    @KafkaListener(
        topics = KafkaTopics.ORDER_COMPLETE,
        groupId ="payment:"+ TopicGroups.ORDER_COMPLETE
    )
    public void consume(OrderPendingMessage message) {

        if (redisRepository.hasIdempotencyKey(message)) {
            logger.info("Idempotency key already exists. message: {}", message);
            return;
        }

        orderPaymentService.pay(
            PaymentCommand.Pay.create(message.orderId(), message.calculatePriceSum()));

        redisRepository.saveIdempotencyKey(message);
    }

}
