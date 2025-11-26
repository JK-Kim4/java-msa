package com.tutomato.userservice.domain.dto;

import com.tutomato.userservice.domain.User;
import com.tutomato.userservice.domain.authentication.Token;
import com.tutomato.userservice.domain.authentication.Tokens;
import com.tutomato.userservice.infrastructure.UserEntity;
import java.time.Instant;
import java.util.List;

public class UserResult {

    public static class Create {

        Long id;
        String userId;
        String email;
        String name;

        public Create(Long id, String userId, String email, String name) {
            this.id = id;
            this.userId = userId;
            this.email = email;
            this.name = name;
        }

        public static Create from(UserEntity entity) {
            return new Create(
                entity.getId(),
                entity.getUserId(),
                entity.getEmail(),
                entity.getName()
            );
        }

        public Long getId() {
            return id;
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
    }

    public static class UserDetail {

        Long id;
        String userId;
        String email;
        String name;
        Instant createdAt;

        List<OrderResult> orders;

        protected UserDetail(Long id, String userId, String email, String name, Instant createdAt) {
            this.id = id;
            this.userId = userId;
            this.email = email;
            this.name = name;
            this.createdAt = createdAt;
        }

        public static UserDetail from(UserEntity entity) {
            return new UserDetail(
                entity.getId(),
                entity.getUserId(),
                entity.getEmail(),
                entity.getName(),
                entity.getCreatedAt()
            );
        }

        public Long getId() {
            return id;
        }

        public String getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public Instant getCreatedAt() {
            return createdAt;
        }

        public List<OrderResult> getOrders() {
            return orders;
        }

        public void setOrders(List<OrderResult> orders) {
            this.orders = orders;
        }
    }


    public static class OrderResult {

        String productId;
        Integer quantity;
        Integer unitPrice;
        Integer totalPrice;
        Instant orderedAt;

        String orderId;

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

    public static class Authentication {

        Tokens tokens;

        public Authentication(Tokens tokens) {
            this.tokens = tokens;
        }

        public Tokens getTokens() {
            return tokens;
        }
    }
}
