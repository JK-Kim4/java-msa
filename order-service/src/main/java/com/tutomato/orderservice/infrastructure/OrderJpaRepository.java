package com.tutomato.orderservice.infrastructure;

import com.tutomato.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

}
