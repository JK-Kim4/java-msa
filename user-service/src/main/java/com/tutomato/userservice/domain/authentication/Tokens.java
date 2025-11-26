package com.tutomato.userservice.domain.authentication;

public record Tokens(
    Token accessToken,
    Token refreshToken
) {

}
