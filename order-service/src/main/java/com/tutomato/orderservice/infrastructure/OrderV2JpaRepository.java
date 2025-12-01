package com.tutomato.orderservice.infrastructure;

import com.tutomato.orderservice.domain.OrderV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderV2JpaRepository extends JpaRepository<OrderEntityV2, Long> {

}
