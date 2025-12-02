package com.tutomato.paymentservice.infrastructure;

import com.tutomato.paymentservice.domain.Payment;
import com.tutomato.paymentservice.domain.PaymentClient;
import org.springframework.stereotype.Component;

@Component
public class ExternalPaymentClient implements PaymentClient {

    @Override
    public String pay(Payment payment) {
        return "";
    }
}
