package com.tutomato.paymentservice.domain.outbox;

import com.tutomato.commonmessaging.common.OutboxStatus;
import com.tutomato.paymentservice.infrastructure.PaymentOutboxJpaRepository;
import com.tutomato.paymentservice.interfaces.PaymentOutboxRow;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentOutboxService {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    public PaymentOutboxService(PaymentOutboxJpaRepository paymentOutboxJpaRepository) {
        this.paymentOutboxJpaRepository = paymentOutboxJpaRepository;
    }

    public List<PaymentOutboxRow> claimTop100() {
        Pageable pageable = PageRequest.of(0, 100);
        return paymentOutboxJpaRepository.findClaimTop100(
            List.of(OutboxStatus.PENDING, OutboxStatus.FAILED), pageable);
    }

    public List<PaymentOutbox> findTop100PendingList() {
        Pageable pageable = PageRequest.of(0, 100);
        return paymentOutboxJpaRepository.findPendingOutbox(OutboxStatus.PENDING, pageable);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPublished(Long id) {
        paymentOutboxJpaRepository.updateStatusToSent(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Long outboxId, String error) {
        paymentOutboxJpaRepository.updateStatusToFail(outboxId);
        // TODO logging fail reason
    }
}
