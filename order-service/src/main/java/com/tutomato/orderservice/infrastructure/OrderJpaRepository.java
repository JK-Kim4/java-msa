package com.tutomato.orderservice.infrastructure;

import com.tutomato.orderservice.domain.Order;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Query("""
              select o
              from Order o
              where o.orderId = :orderId
        """)
    Optional<Order> findByOrderId(@Param("orderId") String orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
              select o
              from Order o
              where o.orderId = :orderId
        """)
    Optional<Order> findByOrderIdWithPessimisticLock(@Param("orderId") String orderId);


    @Query("""
            select o
            from Order o
            where o.userId = :userId
        """)
    List<Order> findByUserId(@Param("userId") String userId);
}
