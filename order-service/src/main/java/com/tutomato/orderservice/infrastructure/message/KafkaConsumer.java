package com.tutomato.orderservice.infrastructure.message;

import com.tutomato.commonmessaging.catalog.DecreaseStockCompleteMessage;
import com.tutomato.commonmessaging.catalog.DecreaseStockFailMessage;
import com.tutomato.commonmessaging.order.OrderPendingMessage;
import com.tutomato.commonmessaging.payment.PaymentFailMessage;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import com.tutomato.commonmessaging.topic.KafkaTopics.TopicGroups;
import com.tutomato.orderservice.domain.OrderService;
import com.tutomato.orderservice.infrastructure.lock.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final OrderService orderService;
    private final RedisRepository redisRepository;

    public KafkaConsumer(
        OrderService orderService,
        RedisRepository redisRepository
    ) {
        this.orderService = orderService;
        this.redisRepository = redisRepository;
    }


    @KafkaListener(
        topics = KafkaTopics.CATALOG_STOCK_DECREASE,
        groupId = "order:" + TopicGroups.STOCK_DECREASE_SUCCESS
    )
    public void consume(DecreaseStockCompleteMessage message) {

        if (redisRepository.hasIdempotencyKey(message)) {
            logger.info("Idempotency key already exists. message: {}", message);
            return;
        }

        orderService.decreaseStockComplete(message.orderId());

        redisRepository.saveIdempotencyKey(message);
    }

    @KafkaListener(
        topics = KafkaTopics.CATALOG_STOCK_DECREASE_FAIL,
        groupId = "order:" + TopicGroups.STOCK_DECREASE_FAIL
    )
    public void consume(DecreaseStockFailMessage message) {

        if (redisRepository.hasIdempotencyKey(message)) {
            logger.info("Idempotency key already exists. message: {}", message);
            return;
        }

        orderService.decreaseStockFail(message.orderId());

        redisRepository.saveIdempotencyKey(message);
    }

    @KafkaListener(
        topics = KafkaTopics.PAYMENT_SUCCESS,
        groupId = "order:" + TopicGroups.PAYMENT_SUCCESS
    )
    public void consume(PaymentSuccessMessage message) {

        if (redisRepository.hasIdempotencyKey(message)) {
            logger.info("Idempotency key already exists. message: {}", message);
            return;
        }

        orderService.paymentSuccess(message.orderId());

        redisRepository.saveIdempotencyKey(message);
    }


    @KafkaListener(
        topics = KafkaTopics.PAYMENT_FAIL,
        groupId = "order:" + TopicGroups.PAYMENT_FAIL
    )
    public void consume(PaymentFailMessage message) {

        if (redisRepository.hasIdempotencyKey(message)) {
            logger.info("Idempotency key already exists. message: {}", message);
            return;
        }

        orderService.paymentFail(message.orderId());

        redisRepository.saveIdempotencyKey(message);
    }
}
