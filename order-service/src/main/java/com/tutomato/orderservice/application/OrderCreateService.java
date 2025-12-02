package com.tutomato.orderservice.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.commonmessaging.common.AggregateType;
import com.tutomato.commonmessaging.order.CommonOrderLine;
import com.tutomato.commonmessaging.order.OrderPendingMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.domain.OrderLine;
import com.tutomato.orderservice.domain.dto.OrderResult;
import com.tutomato.orderservice.domain.dto.OrderCommand;
import com.tutomato.orderservice.infrastructure.OrderOutboxRepository;
import com.tutomato.orderservice.infrastructure.OrderRepository;
import com.tutomato.orderservice.infrastructure.message.OrderOutbox;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderCreateService implements OrderCreateUseCase {

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final OrderOutboxRepository orderOutboxRepository;

    public OrderCreateService(
            ObjectMapper objectMapper,
            OrderRepository orderRepository,
            OrderOutboxRepository orderOutboxRepository
    ) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.orderOutboxRepository = orderOutboxRepository;
    }

    @Override
    public OrderResult create(OrderCommand.Create command) {

        Order order = orderRepository.save(command.toEntity());

        List<OrderLine> orderLines = command.getOrderLines().stream()
                .map(lineDto -> {
                    return OrderLine.create(order, lineDto);
                }).toList();

        orderRepository.saveOrderLines(orderLines);
        order.allocateOrderLines(orderLines);

        OrderOutbox outbox = createPendingOutbox(order);
        orderOutboxRepository.save(outbox);

        return OrderResult.from(order);
    }


    private OrderOutbox createPendingOutbox(Order order) {
        List<CommonOrderLine> orderLines = order.getOrderLines().stream().map(
                orderLine -> {
                    return new CommonOrderLine(orderLine.getProductId(), orderLine.getQuantity(), orderLine.getUnitPrice());
                }
        ).toList();

        String payload = toJson(new OrderPendingMessage(
                order.getOrderId(),
                orderLines
        ));

        return OrderOutbox.pending(
                AggregateType.ORDER,
                order.getOrderId(),
                KafkaTopics.ORDER_COMPLETE,
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
