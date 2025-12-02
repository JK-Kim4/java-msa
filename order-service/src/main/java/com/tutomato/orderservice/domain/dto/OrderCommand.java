package com.tutomato.orderservice.domain.dto;

import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequest.OrderLineDto;

import java.util.List;
import java.util.UUID;

public class OrderCommand {

    public static class Create {

        String userId;
        List<OrderLineDto> orderLineDtos;

        protected Create() {
        }

        protected Create(String userId, List<OrderLineDto> orderLineDtos) {
            this.userId = userId;
            this.orderLineDtos = orderLineDtos;
        }

        public static Create of(String userId, List<OrderLineDto> orderLineDtos) {
            return new Create(userId, orderLineDtos);
        }

        public Order toEntity() {
            String orderId = UUID.randomUUID().toString();

            return Order.create(this.userId, orderId);
        }

        public List<OrderLineDto> getOrderLines() {
            return orderLineDtos;
        }

        public String getUserId() {
            return userId;
        }
    }
}
