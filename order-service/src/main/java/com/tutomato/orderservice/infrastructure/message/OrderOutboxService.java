package com.tutomato.orderservice.infrastructure.message;

import com.tutomato.commonmessaging.common.OutboxStatus;
import com.tutomato.orderservice.infrastructure.OrderOutboxRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderOutboxService {

    private final OrderOutboxRepository orderOutboxRepository;

    public OrderOutboxService(OrderOutboxRepository orderOutboxRepository) {
        this.orderOutboxRepository = orderOutboxRepository;
    }

    public List<OrderOutbox> findTop100PendingList() {
        Pageable pageable = PageRequest.of(0, 100);
        return orderOutboxRepository.findPendingOutbox(OutboxStatus.PENDING, pageable);
    }

}
