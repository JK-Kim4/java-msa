package com.tutomato.paymentservice.infrastructure.message;

import com.tutomato.commonmessaging.payment.PaymentFailMessage;
import com.tutomato.commonmessaging.payment.PaymentSuccessMessage;

public interface PaymentMessagePublisher {

    void send(PaymentSuccessMessage message);

    void fail(PaymentFailMessage message);

}
