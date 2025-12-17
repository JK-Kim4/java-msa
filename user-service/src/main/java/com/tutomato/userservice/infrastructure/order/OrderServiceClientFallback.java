package com.tutomato.userservice.infrastructure.order;

import com.tutomato.userservice.domain.dto.OrderResponse;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceClientFallback implements OrderServiceClient {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceClientFallback.class);

    @Override
    public List<OrderResponse> getOrders(String userId) {
        logger.warn("order-service.getOrders() fallback 호출. userId={}", userId);

        return Collections.emptyList();
    }
}