package com.tutomato.orderservice.interfaces;

import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.domain.OrderService;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequest;
import com.tutomato.orderservice.interfaces.dto.OrderResponse;
import java.util.List;
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

    private final OrderService orderService;
    private final Environment environment;

    public OrderApiController(OrderService orderService, Environment environment) {
        this.orderService = orderService;
        this.environment = environment;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return String.format("Order service is running on local port %s (server port: %s) \n bus test: %s",
            environment.getProperty("local.server.port"),
            environment.getProperty("local.server.port"),
            environment.getProperty("bus.test"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResponse> create(
        @PathVariable(name = "userId") String userId,
        @RequestBody CreateOrderRequest request
    ) {
        Order order = orderService.create(request.toCommand(userId));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(OrderResponse.from(order));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(
        @PathVariable(name = "userId") String userId
    ) {
        List<Order> orders = orderService.findByUserId(userId);

        return ResponseEntity.ok(orders.stream().map(OrderResponse::from).toList());
    }

}
