package com.tutomato.gatewayserver.common;

import java.time.Instant;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
    int status,
    String code,
    String message,
    String path,
    Instant timestamp
) {
    public static ErrorResponse of(HttpStatus status, String code, String message, String path) {
        return new ErrorResponse(
            status.value(),
            code,
            message,
            path,
            Instant.now()
        );
    }
}
