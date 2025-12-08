package com.tutomato.orderservice.interfaces;

import com.tutomato.orderservice.application.OrderCreateService;
import com.tutomato.orderservice.domain.dto.OrderResult;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequest;
import com.tutomato.orderservice.interfaces.dto.OrderResponseV2;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {

    private final OrderCreateService orderCreateService;
    private final Environment environment;

    public OrderApiController(
        OrderCreateService orderCreateService,
        Environment environment
    ) {
        this.environment = environment;
        this.orderCreateService = orderCreateService;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return String.format(
            "Order service is running on local port %s (server port: %s) \n bus test: %s",
            environment.getProperty("local.server.port"),
            environment.getProperty("local.server.port"),
            environment.getProperty("bus.test"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResponseV2> create(
        @PathVariable(name = "userId") String userId,
        @RequestBody CreateOrderRequest request
    ) {
        OrderResult order = orderCreateService.create(request.toCommand(userId));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(OrderResponseV2.from(order));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<> findOrders(
            @PathVariable(name = "userId") String userId
    ) {

    }

}
