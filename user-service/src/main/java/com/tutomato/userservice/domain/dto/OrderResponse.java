package com.tutomato.userservice.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private String orderId;
    private String userId;
    private List<OrderLineDto> orderLines;
    private LocalDateTime createdAt;

    protected OrderResponse() {}

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public List<OrderLineDto> getOrderLines() {
        return orderLines;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    private static class OrderLineDto {
        String productId;
        Integer quantity;
        Integer unitPrice;

        protected OrderLineDto() {}

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
