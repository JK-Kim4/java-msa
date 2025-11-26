package com.tutomato.gatewayserver.authentication;

public record Tokens(
    Token accessToken,
    Token refreshToken
) {

}
