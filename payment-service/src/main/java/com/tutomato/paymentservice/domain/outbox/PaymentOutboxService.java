package com.tutomato.paymentservice.domain.outbox;

import com.tutomato.commonmessaging.common.OutboxStatus;
import com.tutomato.paymentservice.infrastructure.PaymentJpaRepository;
import com.tutomato.paymentservice.infrastructure.PaymentOutboxJpaRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentOutboxService {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    public PaymentOutboxService(PaymentOutboxJpaRepository paymentOutboxJpaRepository) {
        this.paymentOutboxJpaRepository = paymentOutboxJpaRepository;
    }


    public List<PaymentOutbox> findTop100PendingList() {
        Pageable pageable = PageRequest.of(0, 100);
        return paymentOutboxJpaRepository.findPendingOutbox(OutboxStatus.PENDING, pageable);
    }

}
