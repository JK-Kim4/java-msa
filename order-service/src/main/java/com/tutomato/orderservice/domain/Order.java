package com.tutomato.orderservice.domain;


import com.tutomato.orderservice.domain.dto.OrderCommand;
import com.tutomato.orderservice.infrastructure.OrderEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Order {

    private String productId;
    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;
    private String userId;
    private String orderId;
    private LocalDateTime createdAt;

    protected Order() {
    }

    protected Order(String productId, Integer quantity, Integer unitPrice, Integer totalPrice,
        String userId, String orderId) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.orderId = orderId;
    }

    protected Order(String productId, Integer quantity, Integer unitPrice, Integer totalPrice,
        String userId, String orderId, LocalDateTime createdAt) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public static Order fromCommand(OrderCommand.Create command) {
        return new Order(
            command.getProductId(),
            command.getQuantity(),
            command.getUnitPrice(),
            command.getQuantity() * command.getUnitPrice(),
            command.getUserId(),
            java.util.UUID.randomUUID().toString()
        );
    }


    public static Order fromEntity(OrderEntity entity) {
        return new Order(
            entity.getProductId(),
            entity.getQuantity(),
            entity.getUnitPrice(),
            entity.getTotalPrice(),
            entity.getUserId(),
            entity.getOrderId(),
            LocalDateTime.ofInstant(entity.getCreatedAt(), ZoneId.of("Asia/Seoul"))
        );
    }

    public OrderEntity toEntity() {
        return new OrderEntity(
            this.productId,
            this.quantity,
            this.unitPrice,
            this.totalPrice,
            this.userId,
            this.orderId
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
