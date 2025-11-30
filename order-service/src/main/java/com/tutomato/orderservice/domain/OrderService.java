package com.tutomato.orderservice.domain;

import com.tutomato.commonmessaging.order.OrderIssuedMessage;
import com.tutomato.orderservice.domain.dto.OrderCommand;
import com.tutomato.orderservice.infrastructure.OrderJpaRepository;
import com.tutomato.orderservice.infrastructure.message.OrderMessagePublisher;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderMessagePublisher orderMessagePublisher;

    public OrderService(OrderJpaRepository orderJpaRepository, OrderMessagePublisher orderMessagePublisher) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderMessagePublisher = orderMessagePublisher;
    }

    public Order create(OrderCommand.Create command) {
        Order order = Order.fromCommand(command);

        orderJpaRepository.save(order.toEntity());

        orderMessagePublisher.send(new OrderIssuedMessage(order.getOrderId(), order.getProductId(), order.getQuantity()));

        return order;
    }

    public Order findByOrderId(String orderId) {
        return Order.fromEntity(orderJpaRepository.findByOrderId(orderId));
    }

    public List<Order> findByUserId(String userId) {
        return orderJpaRepository.findByUserId(userId).stream()
            .map(Order::fromEntity)
            .toList();
    }

}
