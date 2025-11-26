package com.tutomato.userservice.domain.authentication;

import java.time.Instant;

public record Token(
    String value,
    TokenType tokenType,
    Instant issuedAt,
    Instant expiration
) {

}
