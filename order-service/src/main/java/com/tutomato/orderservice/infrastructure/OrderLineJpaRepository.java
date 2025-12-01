package com.tutomato.orderservice.infrastructure;

import com.tutomato.orderservice.interfaces.dto.CreateOrderRequestV2.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineJpaRepository extends JpaRepository<OrderLineEntity, Long> {

}
