package com.tutomato.orderservice.application;

import com.tutomato.orderservice.domain.dto.OrderResult;
import com.tutomato.orderservice.domain.dto.OrderCommand;

public interface OrderCreateUseCase {

    OrderResult create(OrderCommand.Create command);

}
