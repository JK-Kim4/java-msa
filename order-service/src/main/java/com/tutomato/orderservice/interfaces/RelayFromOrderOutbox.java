package com.tutomato.orderservice.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.commonmessaging.order.OrderIssuedMessage;
import com.tutomato.commonmessaging.order.OrderPendingMessage;
import com.tutomato.orderservice.infrastructure.message.OrderMessagePublisher;
import com.tutomato.orderservice.infrastructure.message.OrderOutbox;
import com.tutomato.orderservice.infrastructure.message.OrderOutboxService;
import java.util.List;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RelayFromOrderOutbox {

    private final OrderMessagePublisher orderMessagePublisher;
    private final OrderOutboxService orderOutboxService;
    private final ObjectMapper objectMapper;

    public RelayFromOrderOutbox(
        OrderMessagePublisher orderMessagePublisher,
        OrderOutboxService orderOutboxService,
        ObjectMapper objectMapper) {
        this.orderMessagePublisher = orderMessagePublisher;
        this.orderOutboxService = orderOutboxService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @Scheduled(initialDelay = 5000L, fixedDelay = 1000L)
    @SchedulerLock(
        name = "orderOutboxRelayJob",
        lockAtMostFor = "5s",
        lockAtLeastFor = "1s"
    )
    public void relay() {

        List<OrderOutbox> outboxes = orderOutboxService.findTop100PendingList();

        outboxes.forEach(orderOutbox -> {
            try {
                OrderPendingMessage message =
                    objectMapper.readValue(orderOutbox.getPayload(), OrderPendingMessage.class);

                orderMessagePublisher.send(orderOutbox.getEventType(), message.orderId(), message);
                orderOutbox.markPublished();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
