package com.tutomato.paymentservice.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.commonmessaging.common.AggregateType;
import com.tutomato.commonmessaging.order.OrderIssuedMessage;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import com.tutomato.paymentservice.application.PaymentResult.Pay;
import com.tutomato.paymentservice.domain.Payment;
import com.tutomato.paymentservice.domain.PaymentClient;
import com.tutomato.paymentservice.domain.outbox.PaymentOutbox;
import com.tutomato.paymentservice.infrastructure.PaymentJpaRepository;
import com.tutomato.paymentservice.infrastructure.PaymentOutboxJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPaymentService implements PaymentPayUseCase {

    private final ObjectMapper objectMapper;
    private final PaymentClient paymentClient;
    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    public OrderPaymentService(
        ObjectMapper objectMapper,
        PaymentClient paymentClient,
        PaymentJpaRepository paymentJpaRepository,
        PaymentOutboxJpaRepository paymentOutboxJpaRepository
    ) {
        this.objectMapper = objectMapper;
        this.paymentClient = paymentClient;
        this.paymentJpaRepository = paymentJpaRepository;
        this.paymentOutboxJpaRepository = paymentOutboxJpaRepository;
    }

    @Override
    @Transactional
    public Pay pay(PaymentCommand.Pay command) {
        Payment payment = paymentJpaRepository.save(command.toEntity());

        paymentClient.pay(payment);

        PaymentOutbox outbox = paymentOutboxJpaRepository.save(createOutbox(payment));

        return PaymentResult.Pay.from(payment);
    }

    private PaymentOutbox createOutbox(Payment payment) {
        String payload = toJson(new PaymentSuccessMessage(
            payment.getPaymentId(),
            payment.getOrderId(),
            payment.getPaymentPrice()
        ));

        return PaymentOutbox.pending(
            AggregateType.PAYMENT,
            payment.getPaymentId(),
            KafkaTopics.PAYMENT_SUCCESS,
            payload
        );
    }

    private String toJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize outbox payload", e);
        }
    }
}
