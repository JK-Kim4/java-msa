package com.tutomato.orderservice.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    OrderEntity findByOrderId(String orderId);

    List<OrderEntity> findByUserId(String userId);
}
