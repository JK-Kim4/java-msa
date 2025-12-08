package com.tutomato.orderservice.domain;

import com.tutomato.orderservice.domain.dto.OrderResult;
import com.tutomato.orderservice.infrastructure.OrderRepository;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Retryable(
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

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
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

    public List<OrderResult> findByUserId(String userId) {
        logger.info("GET ORDER LIST BY USER ID START {}", userId);

        List<Order> orders = orderRepository.findByUserId(userId);

        logger.info("GET ORDER LIST BY USER ID END {}", userId);
        return OrderResult.from(orders);
    }
}
