package com.tutomato.orderservice.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.commonmessaging.common.AggregateType;
import com.tutomato.commonmessaging.order.OrderIssuedMessage;
import com.tutomato.commonmessaging.order.OrderLine;
import com.tutomato.commonmessaging.order.OrderPendingMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.domain.OrderV2;
import com.tutomato.orderservice.domain.dto.OrderCommand;
import com.tutomato.orderservice.domain.dto.OrderCommand.Create;
import com.tutomato.orderservice.infrastructure.OrderEntityV2;
import com.tutomato.orderservice.infrastructure.OrderJpaRepository;
import com.tutomato.orderservice.infrastructure.OrderOutboxRepository;
import com.tutomato.orderservice.infrastructure.OrderRepository;
import com.tutomato.orderservice.infrastructure.message.OrderOutbox;
import java.util.ArrayList;
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
    public OrderV2 create(OrderCommand.CreateV2 command) {

        OrderV2 orderV2 = OrderV2.from(command);
        orderRepository.save(orderV2);

        OrderOutbox outbox = createPendingOutbox(orderV2);
        orderOutboxRepository.save(outbox);

        return orderV2;
    }


    private OrderOutbox createPendingOutbox(OrderV2 order) {
        List<OrderLine> orderLines = order.getOrderLines().stream().map(
            orderLine -> {
                return new OrderLine(orderLine.getProductId(), orderLine.getUnitPrice(), orderLine.getQuantity());
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
