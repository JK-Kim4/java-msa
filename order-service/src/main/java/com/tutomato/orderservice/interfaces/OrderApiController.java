package com.tutomato.orderservice.interfaces;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {

    private final Environment environment;

    public OrderApiController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return String.format("Catalog service is running on local port %s (server port: %s)",
            environment.getProperty("local.server.port"),
            environment.getProperty("local.server.port"));
    }

}
