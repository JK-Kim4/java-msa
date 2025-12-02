package com.tutomato.orderservice.application;

import com.tutomato.orderservice.domain.OrderV2;
import com.tutomato.orderservice.domain.dto.OrderCommand;

public interface OrderCreateUseCase {

    OrderV2 create(OrderCommand.CreateV2 command);

}
