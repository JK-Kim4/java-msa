package com.tutomato.orderservice.application;

import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.domain.dto.OrderCommand;

public interface OrderCreateUseCase {

    Order create(OrderCommand.Create command);

}
