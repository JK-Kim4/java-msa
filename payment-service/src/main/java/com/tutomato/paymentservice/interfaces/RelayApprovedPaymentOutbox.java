package com.tutomato.paymentservice.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;
import com.tutomato.paymentservice.domain.outbox.PaymentOutbox;
import com.tutomato.paymentservice.domain.outbox.PaymentOutboxService;
import com.tutomato.paymentservice.infrastructure.message.PaymentMessagePublisher;
import java.util.List;
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
    @SchedulerLock(
        name = "pamynetOutboxRelayJob",
        lockAtMostFor = "5s",
        lockAtLeastFor = "1s"
    )
    public void relay() {

        List<PaymentOutbox> outboxes = paymentOutboxService.findTop100PendingList();

        outboxes.forEach(paymentOutbox -> {
            try {

                PaymentSuccessMessage message =
                    objectMapper.readValue(paymentOutbox.getPayload(), PaymentSuccessMessage.class);

                paymentMessagePublisher.send(message);
                paymentOutbox.markPublished();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        });
    }

}
