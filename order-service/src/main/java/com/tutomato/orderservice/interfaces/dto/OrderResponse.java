package com.tutomato.orderservice.interfaces.dto;

import com.tutomato.orderservice.domain.Order;
import java.time.LocalDateTime;

public class OrderResponse {

    private String productId;
    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;
    private String userId;
    private String orderId;
    private LocalDateTime createdAt;

    protected OrderResponse() {
    }

    protected OrderResponse(String productId, Integer quantity, Integer unitPrice,
        Integer totalPrice, String userId, String orderId, LocalDateTime createdAt) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getProductId(),
            order.getQuantity(),
            order.getUnitPrice(),
            order.getTotalPrice(),
            order.getUserId(),
            order.getOrderId(),
            order.getCreatedAt()
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

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
