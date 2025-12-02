package com.tutomato.orderservice.interfaces.dto;

import com.tutomato.orderservice.domain.OrderLine;
import com.tutomato.orderservice.domain.dto.OrderCommand;

import java.util.List;

public class CreateOrderRequest {

    List<OrderLineDto> orderLines;

    protected CreateOrderRequest() {
    }

    public OrderCommand.Create toCommand(String userId) {
        return OrderCommand.Create.of(userId, orderLines);
    }

    public List<OrderLineDto> getOrderLines() {
        return orderLines;
    }

    public static class OrderLineDto {

        String productId;
        Integer quantity;
        Integer unitPrice;

        protected OrderLineDto() {
        }

        protected OrderLineDto(String productId, Integer quantity, Integer unitPrice) {
            this.productId = productId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public static OrderLineDto from(OrderLine orderLine) {
            return new OrderLineDto(
                orderLine.getProductId(),
                orderLine.getQuantity(),
                orderLine.getUnitPrice()
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
}
