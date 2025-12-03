package com.tutomato.orderservice.domain;

import com.tutomato.orderservice.infrastructure.OrderRepository;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Retryable(
    // 재시도 대상 예외
    value = {
        PessimisticLockingFailureException.class,
        CannotAcquireLockException.class,
        LockTimeoutException.class,
        PessimisticLockException.class
    },
    maxAttempts = 5,
    backoff = @Backoff(delay = 100)
)
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public void decreaseStockComplete(String orderId) {
        Order order = orderRepository.findByOrderIdWithPessimisticLock(orderId);

        order.stockDecreased();

        if (order.isOrderTransactionCompleted()) {
            order.completed();
        } else {
            order.pending();
        }
    }

    public void decreaseStockFail(String orderId) {
        Order order = orderRepository.findByOrderIdWithPessimisticLock(orderId);

        order.failed();

        //todo fail reason
    }

    public void paymentSuccess(String orderId) {
        Order order = orderRepository.findByOrderIdWithPessimisticLock(orderId);

        order.paid();

        if (order.isOrderTransactionCompleted()) {
            order.completed();
        } else {
            order.pending();
        }
    }

    public void paymentFail(String orderId) {
        Order order = orderRepository.findByOrderIdWithPessimisticLock(orderId);

        order.failed();
    }
}
