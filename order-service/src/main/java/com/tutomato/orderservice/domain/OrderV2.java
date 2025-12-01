package com.tutomato.orderservice.domain;

import com.tutomato.orderservice.domain.dto.OrderCommand;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequestV2.OrderLine;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderV2 {

    private String orderId;
    private String userId;
    private List<OrderLine> orderLines;
    private LocalDateTime createdAt;

    protected OrderV2(String orderId, String userId, List<OrderLine> orderLines) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderLines = orderLines;
        this.createdAt = LocalDateTime.now();
    }

    public static OrderV2 from(OrderCommand.CreateV2 command) {
        return new OrderV2(
            UUID.randomUUID().toString(),
            command.getUserId(),
            command.getOrderLines()
        );
    }


    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
