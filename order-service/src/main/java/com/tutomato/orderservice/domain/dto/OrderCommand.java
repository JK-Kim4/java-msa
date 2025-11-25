package com.tutomato.orderservice.domain.dto;

import com.tutomato.orderservice.interfaces.dto.CreateOrderRequest;
import java.util.UUID;

public class OrderCommand {

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
