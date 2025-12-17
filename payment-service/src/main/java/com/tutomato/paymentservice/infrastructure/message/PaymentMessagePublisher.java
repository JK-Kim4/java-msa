package com.tutomato.paymentservice.infrastructure.message;

import com.tutomato.commonmessaging.payment.PaymentFailMessage;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;
import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.support.SendResult;

public interface PaymentMessagePublisher {

    CompletableFuture<SendResult<String, Object>> send(PaymentSuccessMessage message);

    CompletableFuture<SendResult<String, Object>> fail(PaymentFailMessage message);

}
