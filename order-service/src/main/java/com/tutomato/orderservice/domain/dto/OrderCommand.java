package com.tutomato.orderservice.domain.dto;

import com.tutomato.orderservice.domain.OrderV2;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequest;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequestV2.OrderLine;
import java.util.List;
import java.util.UUID;

public class OrderCommand {

    public static class CreateV2 {

        List<OrderLine> orderLines;
        String userId;

        public CreateV2(List<OrderLine> orderLines, String userId) {
            this.orderLines = orderLines;
            this.userId = userId;
        }

        public List<OrderLine> getOrderLines() {
            return orderLines;
        }

        public String getUserId() {
            return userId;
        }
    }

    public static class Create {

        String productId;
        Integer quantity;
        Integer unitPrice;
        String userId;

        public Create(String productId, Integer quantity, Integer unitPrice, String userId) {
            this.productId = productId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.userId = userId;
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

        public String getUserId() {
            return userId;
        }
    }

}
