package com.tutomato.userservice.interfaces.dto;

import com.tutomato.userservice.domain.authentication.Tokens;
import com.tutomato.userservice.domain.dto.UserResult;

public class LoginResponse {

    Tokens tokens;

    protected LoginResponse() {
    }

    protected LoginResponse(Tokens tokens) {
        this.tokens = tokens;
    }

    public static LoginResponse from(UserResult.Authentication result) {
        return new LoginResponse(result.getTokens());
    }

    public Tokens getTokens() {
        return tokens;
    }
}
