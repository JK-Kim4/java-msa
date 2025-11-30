package com.tutomato.catalogservice.infrastructure.message;

import com.tutomato.catalogservice.domain.CatalogService;
import com.tutomato.catalogservice.domain.DecreaseStockCommand;
import com.tutomato.commonmessaging.order.OrderIssuedMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer implements Consumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final CatalogService catalogService;
    private final OrderMessageInMemoryRepository orderMessageInMemoryRepository;

    public KafkaConsumer(CatalogService catalogService, OrderMessageInMemoryRepository orderMessageInMemoryRepository) {
        this.catalogService = catalogService;
        this.orderMessageInMemoryRepository = orderMessageInMemoryRepository;
    }

    @KafkaListener(
        topics = KafkaTopics.ORDER_COMPLETED,
        groupId = KafkaTopics.TopicGroups.ORDER_COMPLETED
    )
    @Override
    public void updateStock(OrderIssuedMessage message) {

        if (orderMessageInMemoryRepository.hasIdempotencyKey(message)) {
            logger.info("Idempotency key already exists. message: {}", message);
            return;
        }

        catalogService.decreaseStock(DecreaseStockCommand.of(message.productId(),  message.decreaseQuantity()));
        orderMessageInMemoryRepository.saveIdempotencyKey(message);
    }
}
