package com.tutomato.orderservice.domain.dto;

import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequest.OrderLineDto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResult {

    private String orderId;
    private String userId;
    private List<OrderLineDto> orderLines;
    private LocalDateTime createdAt;

    protected OrderResult(String orderId, String userId, List<OrderLineDto> orderLines,
        LocalDateTime createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderLines = orderLines;
        this.createdAt = LocalDateTime.now();
    }

    public static OrderResult from(Order order) {
        List<OrderLineDto> orderLines = order.getOrderLines().stream().map(OrderLineDto::from)
            .toList();

        return new OrderResult(
            order.getOrderId(),
            order.getUserId(),
            orderLines,
            LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of("Asia/Seoul"))
        );
    }


    public static List<OrderResult> from(List<Order> orders) {
        return orders.stream().map(OrderResult::from).collect(Collectors.toList());
    }

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
}
