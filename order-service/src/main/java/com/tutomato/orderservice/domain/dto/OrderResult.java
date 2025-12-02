package com.tutomato.orderservice.domain.dto;

import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequest.OrderLineDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class OrderResult {

    private String orderId;
    private String userId;
    private List<OrderLineDto> orderLineDtos;
    private LocalDateTime createdAt;

    protected OrderResult(String orderId, String userId, List<OrderLineDto> orderLineDtos, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderLineDtos = orderLineDtos;
        this.createdAt = LocalDateTime.now();
    }

    public static OrderResult from(Order order) {
        List<OrderLineDto> orderLineDtos = order.getOrderLines().stream().map(OrderLineDto::from).toList();

        return new OrderResult(
                order.getOrderId(),
                order.getUserId(),
                orderLineDtos,
                LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of("Asia/Seoul"))
        );
    }


    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public List<OrderLineDto> getOrderLines() {
        return orderLineDtos;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
