package com.tutomato.orderservice.infrastructure;

import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.domain.OrderLine;

import jakarta.persistence.NoResultException;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderLineJpaRepository orderLineJpaRepository;

    public OrderRepository(
            OrderJpaRepository orderJpaRepository,
            OrderLineJpaRepository orderLineJpaRepository
    ) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderLineJpaRepository = orderLineJpaRepository;
    }

    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    public List<OrderLine> saveOrderLines(List<OrderLine> orderLines) {
        return orderLineJpaRepository.saveAll(orderLines);
    }

    public Order findByOrderId(String orderId) {
        return orderJpaRepository.findByOrderId(orderId)
            .orElseThrow(NoResultException::new);
    }

    public Order findByOrderIdWithPessimisticLock(String orderId) {
        return orderJpaRepository.findByOrderIdWithPessimisticLock(orderId)
            .orElseThrow(NoResultException::new);
    }

    public List<Order> findByUserId(String userId) {
        return orderJpaRepository.findByUserId(userId);
    }
}
