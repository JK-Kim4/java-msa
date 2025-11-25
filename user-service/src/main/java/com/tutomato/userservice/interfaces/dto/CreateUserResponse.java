package com.tutomato.userservice.interfaces.dto;

import com.tutomato.userservice.domain.dto.UserResult;

public class CreateUserResponse {

    Long id;
    String userId;
    String email;
    String name;

    public CreateUserResponse(Long id, String userId, String email, String name) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public static CreateUserResponse from(UserResult.Create result) {
        return new CreateUserResponse(
            result.getId(),
            result.getUserId(),
            result.getEmail(),
            result.getName()
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
}
