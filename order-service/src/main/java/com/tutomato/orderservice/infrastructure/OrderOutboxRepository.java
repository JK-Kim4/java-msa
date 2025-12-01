package com.tutomato.orderservice.infrastructure;

import com.tutomato.orderservice.infrastructure.message.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long> {

}
