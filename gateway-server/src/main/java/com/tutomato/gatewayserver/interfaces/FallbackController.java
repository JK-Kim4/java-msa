package com.tutomato.gatewayserver.interfaces;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping(value = "/fallback/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> ordersFallback() {
        // 간단한 JSON 응답 예시
        return Mono.just("{\"message\": \"Order service is temporarily unavailable. Please try again later.\"}");
    }
}
