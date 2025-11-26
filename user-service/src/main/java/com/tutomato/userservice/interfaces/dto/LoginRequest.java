package com.tutomato.userservice.interfaces.dto;

import com.tutomato.userservice.domain.dto.UserCommand.Authentication;

public class LoginRequest {

    String email;
    String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Authentication toCommand() {
        return Authentication.of(email, password);
    }
}
