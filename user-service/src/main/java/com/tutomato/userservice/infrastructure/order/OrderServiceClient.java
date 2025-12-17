package com.tutomato.userservice.infrastructure.order;

import com.tutomato.userservice.domain.dto.OrderResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "order-service",
    fallback = OrderServiceClientFallback.class
)
public interface OrderServiceClient {

    @GetMapping("/{userId}/orders")
    List<OrderResponse> getOrders(@PathVariable String userId);

}