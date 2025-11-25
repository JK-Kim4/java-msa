package com.tutomato.orderservice.interfaces.dto;

import com.tutomato.orderservice.domain.dto.OrderCommand;
import java.util.UUID;

public class CreateOrderRequest {

    String productId;
    Integer quantity;
    Integer unitPrice;

    public OrderCommand.Create toCommand(String userId) {
        return new OrderCommand.Create(
            productId,
            quantity,
            unitPrice,
            userId
        );
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }
}
