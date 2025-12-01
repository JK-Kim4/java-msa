package com.tutomato.orderservice.interfaces.dto;

import com.tutomato.orderservice.domain.dto.OrderCommand;
import com.tutomato.orderservice.domain.dto.OrderCommand.CreateV2;
import java.util.List;

public class CreateOrderRequestV2 {

    List<OrderLine> orderLines;

    public OrderCommand.CreateV2 toCommand(String userId) {
        return new CreateV2(orderLines, userId);
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public static class OrderLine {

        String productId;
        Integer quantity;
        Integer unitPrice;

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
}
