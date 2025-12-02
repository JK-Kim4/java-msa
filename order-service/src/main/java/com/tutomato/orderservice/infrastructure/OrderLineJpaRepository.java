package com.tutomato.orderservice.infrastructure;

import com.tutomato.orderservice.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineJpaRepository extends JpaRepository<OrderLine, Long> {

}
