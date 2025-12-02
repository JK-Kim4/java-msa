package com.tutomato.orderservice.infrastructure;

import com.tutomato.orderservice.domain.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Query("""
              select o
              from Order o
              where o.orderId = :orderId
        """)
    Optional<Order> findByOrderId(@Param("orderId") String orderId);
}
