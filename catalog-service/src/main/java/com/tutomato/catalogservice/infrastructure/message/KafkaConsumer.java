package com.tutomato.catalogservice.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.catalogservice.application.DecreaseStockService;
import com.tutomato.catalogservice.domain.CatalogService;
import com.tutomato.catalogservice.domain.DecreaseStockCommand;
import com.tutomato.catalogservice.domain.outbox.CatalogOutbox;
import com.tutomato.catalogservice.domain.outbox.CatalogOutboxService;
import com.tutomato.commonmessaging.catalog.DecreaseStockCompleteMessage;
import com.tutomato.commonmessaging.catalog.DecreaseStockFailMessage;
import com.tutomato.commonmessaging.common.AggregateType;
import com.tutomato.commonmessaging.order.OrderPendingMessage;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import com.tutomato.commonmessaging.topic.KafkaTopics.TopicGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KafkaConsumer implements Consumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final ObjectMapper objectMapper;
    private final DecreaseStockService decreaseStockService;
    private final CatalogOutboxService catalogOutboxService;
    private final OrderMessageInMemoryRepository orderMessageInMemoryRepository;

    public KafkaConsumer(
        ObjectMapper objectMapper,
        DecreaseStockService decreaseStockService,
        CatalogOutboxService catalogOutboxService,
        OrderMessageInMemoryRepository orderMessageInMemoryRepository
    ) {
        this.objectMapper = objectMapper;
        this.decreaseStockService = decreaseStockService;
        this.catalogOutboxService = catalogOutboxService;
        this.orderMessageInMemoryRepository = orderMessageInMemoryRepository;
    }

    @KafkaListener(
        topics = KafkaTopics.ORDER_COMPLETE,
        groupId = "catalog:" + TopicGroups.ORDER_COMPLETE
    )
    @Override
    public void consume(OrderPendingMessage message) {

        if (orderMessageInMemoryRepository.hasIdempotencyKey(message)) {
            logger.info("Idempotency key already exists. message: {}", message);
            return;
        }

        try {
            message.commonOrderLine().forEach(line -> {
                decreaseStockService.decreaseStock(
                    DecreaseStockCommand.of(line.productId(), line.decreaseQuantity()));
            });

            catalogOutboxService.save(createDecreaseStockMessage(message.orderId()));
        } catch (IllegalArgumentException e) {
            logger.error("Error while processing OrderPendingMessage", e);
            catalogOutboxService.save(createDecreaseStockFailMessage(message.orderId()));
        } finally {
            orderMessageInMemoryRepository.saveIdempotencyKey(message);
        }
    }

    private CatalogOutbox createDecreaseStockMessage(String orderId) {
        String payload = toJson(new DecreaseStockCompleteMessage(
            orderId
        ));

        return CatalogOutbox.pending(
            AggregateType.CATALOG,
            orderId,
            KafkaTopics.CATALOG_STOCK_DECREASE,
            payload
        );
    }

    private CatalogOutbox createDecreaseStockFailMessage(String orderId) {
        String payload = toJson(new DecreaseStockFailMessage(
            orderId
        ));

        return CatalogOutbox.pending(
            AggregateType.CATALOG,
            orderId,
            KafkaTopics.CATALOG_STOCK_DECREASE_FAIL,
            payload
        );
    }

    private String toJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize outbox payload", e);
        }
    }
}
