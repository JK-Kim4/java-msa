package com.tutomato.paymentservice.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.commonmessaging.payment.PaymentFailMessage;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;
import com.tutomato.paymentservice.domain.outbox.PaymentOutboxService;
import com.tutomato.paymentservice.infrastructure.message.PaymentMessagePublisher;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RelayApprovedPaymentOutbox {

    private final Logger logger = LoggerFactory.getLogger(RelayApprovedPaymentOutbox.class);

    private final ObjectMapper objectMapper;
    private final PaymentOutboxService paymentOutboxService;
    private final PaymentMessagePublisher paymentMessagePublisher;

    public RelayApprovedPaymentOutbox(
        ObjectMapper objectMapper,
        PaymentOutboxService paymentOutboxService,
        PaymentMessagePublisher paymentMessagePublisher
    ) {
        this.objectMapper = objectMapper;
        this.paymentOutboxService = paymentOutboxService;
        this.paymentMessagePublisher = paymentMessagePublisher;
    }

    @Transactional
    @Scheduled(initialDelay = 5000L, fixedDelay = 1000L)
    @SchedulerLock(name = "paymentOutboxRelayJob", lockAtMostFor = "3s", lockAtLeastFor = "1s")
    public void relay() {
        List<PaymentOutboxRow> batch = paymentOutboxService.claimTop100();

        for (PaymentOutboxRow row : batch) {
            try {
                CompletableFuture<?> f = row.isSuccess()
                    ? paymentMessagePublisher.send(
                    objectMapper.readValue(row.getPayload(), PaymentSuccessMessage.class))
                    : paymentMessagePublisher.fail(
                        objectMapper.readValue(row.getPayload(), PaymentFailMessage.class));

                f.whenComplete((ok, ex) -> {
                    if (ex == null) {
                        paymentOutboxService.markPublished(row.getId());
                    } else {
                        paymentOutboxService.markFailed(row.getId(), ex.getMessage());
                    }
                });

            } catch (Exception parseEx) {
                paymentOutboxService.markFailed(row.getId(), parseEx.getMessage());
            }
        }
    }

}
