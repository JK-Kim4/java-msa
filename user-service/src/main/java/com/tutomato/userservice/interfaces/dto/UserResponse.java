package com.tutomato.userservice.interfaces.dto;

import com.tutomato.userservice.domain.dto.OrderResponse;
import com.tutomato.userservice.domain.dto.UserResult;
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

        return new UserResponse(
            detail.getEmail(),
            detail.getName(),
            detail.getUserId(),
            detail.getOrders()
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
}
