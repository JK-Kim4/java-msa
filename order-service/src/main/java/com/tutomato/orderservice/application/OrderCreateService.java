package com.tutomato.orderservice.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.commonmessaging.common.AggregateType;
import com.tutomato.commonmessaging.order.OrderIssuedMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.domain.dto.OrderCommand;
import com.tutomato.orderservice.domain.dto.OrderCommand.Create;
import com.tutomato.orderservice.infrastructure.OrderJpaRepository;
import com.tutomato.orderservice.infrastructure.OrderOutboxRepository;
import com.tutomato.orderservice.infrastructure.message.OrderOutbox;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderCreateService implements OrderCreateUseCase {

    private final ObjectMapper objectMapper;
    private final OrderJpaRepository orderJpaRepository;
    private final OrderOutboxRepository orderOutboxRepository;

    public OrderCreateService(
        ObjectMapper objectMapper,
        OrderJpaRepository orderJpaRepository,
        OrderOutboxRepository orderOutboxRepository
    ) {
        this.objectMapper = objectMapper;
        this.orderJpaRepository = orderJpaRepository;
        this.orderOutboxRepository = orderOutboxRepository;
    }


    @Override
    public Order create(OrderCommand.Create command) {

        Order order = Order.fromCommand(command);
        orderJpaRepository.save(order.toEntity());

        OrderOutbox outbox = createPendingOutbox(order);
        orderOutboxRepository.save(outbox);

        return order;
    }

    private OrderOutbox createPendingOutbox(Order order) {

        String payload = toJson(new OrderIssuedMessage(
            order.getOrderId(),
            order.getProductId(),
            order.getQuantity()
        ));

        return OrderOutbox.pending(
            AggregateType.ORDER,
            order.getOrderId(),
            KafkaTopics.ORDER_COMPLETED,
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
