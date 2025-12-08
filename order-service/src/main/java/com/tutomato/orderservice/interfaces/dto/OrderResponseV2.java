package com.tutomato.orderservice.interfaces.dto;

import com.tutomato.orderservice.domain.dto.OrderResult;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequest.OrderLineDto;
import java.util.List;

public class OrderResponseV2 {


    List<OrderLineDto> orderLineDtos;


    public static OrderResponseV2 from(OrderResult orderResult) {
        return null;
    }

}
