package com.tutomato.userservice.interfaces.dto;

import com.tutomato.userservice.domain.dto.UserResult;
import com.tutomato.userservice.domain.dto.UserResult.OrderResult;
import com.tutomato.userservice.infrastructure.UserJpaRepository;
import java.time.Instant;
import java.util.List;

public class UserResponse {

    String email;
    String name;
    String userId;

    List<OrderResponse> orders;

    protected UserResponse() {
    }

    protected UserResponse(String email, String name, String userId, List<OrderResponse> orders) {
        this.email = email;
        this.name = name;
        this.userId = userId;
        this.orders = orders;
    }

    public static UserResponse from(UserResult.UserDetail detail) {
        List<OrderResponse> orderResponses = detail.getOrders().stream()
            .map(OrderResponse::from)
            .toList();

        return new UserResponse(
            detail.getEmail(),
            detail.getName(),
            detail.getUserId(),
            orderResponses
        );
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }

    public static class OrderResponse {

        String productId;
        Integer quantity;
        Integer unitPrice;
        Integer totalPrice;
        Instant orderedAt;

        String orderId;

        protected OrderResponse() {
        }

        protected OrderResponse(String productId, Integer quantity, Integer unitPrice,
            Integer totalPrice, Instant orderedAt, String orderId) {
            this.productId = productId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = totalPrice;
            this.orderedAt = orderedAt;
            this.orderId = orderId;
        }

        public static OrderResponse from(OrderResult orderResult) {

            return new OrderResponse(
                orderResult.getProductId(),
                orderResult.getQuantity(),
                orderResult.getUnitPrice(),
                orderResult.getTotalPrice(),
                orderResult.getOrderedAt(),
                orderResult.getOrderId()
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

        public Instant getOrderedAt() {
            return orderedAt;
        }

        public String getOrderId() {
            return orderId;
        }
    }
}
