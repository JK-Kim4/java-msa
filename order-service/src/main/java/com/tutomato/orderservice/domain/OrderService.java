package com.tutomato.orderservice.domain;

import com.tutomato.orderservice.infrastructure.OrderJpaRepository;
import com.tutomato.orderservice.infrastructure.OrderRepository;
import com.tutomato.orderservice.infrastructure.lock.DistributedLock;
import com.tutomato.orderservice.infrastructure.lock.LockKey;
import com.tutomato.orderservice.infrastructure.lock.RedisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @DistributedLock(
        key = LockKey.UPDATE_ORDER,
        keyValue = "#orderId",
        retryCount = 5,
        retryDelay = 300
    )
    public void decreaseStockComplete(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);

        order.stockDecreased();

        if (order.isOrderTransactionCompleted()) {
            order.completed();
        } else {
            order.pending();
        }
    }

    @DistributedLock(
        key = LockKey.UPDATE_ORDER,
        keyValue = "#orderId",
        retryCount = 5,
        retryDelay = 300
    )
    public void decreaseStockFail(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);

        order.failed();

        //todo fail reason
    }

    @DistributedLock(
        key = LockKey.UPDATE_ORDER,
        keyValue = "#orderId",
        retryCount = 5,
        retryDelay = 300
    )
    public void paymentSuccess(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);

        order.paid();

        if (order.isOrderTransactionCompleted()) {
            order.completed();
        } else {
            order.pending();
        }
    }

    @DistributedLock(
        key = LockKey.UPDATE_ORDER,
        keyValue = "#orderId",
        retryCount = 5,
        retryDelay = 300
    )
    public void paymentFail(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);

        order.failed();
    }
}
