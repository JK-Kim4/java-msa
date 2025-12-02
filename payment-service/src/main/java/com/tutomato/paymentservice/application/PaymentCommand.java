package com.tutomato.paymentservice.application;

import com.tutomato.paymentservice.domain.Payment;
import java.util.UUID;

public class PaymentCommand {

    public static class Pay {

        String orderId;
        Integer paymentPrice;

        public Payment toEntity() {

            return Payment.create(
                UUID.randomUUID().toString(),
                orderId,
                paymentPrice
            );
        }

        protected Pay() {
        }

        protected Pay(String orderId, Integer paymentPrice) {
            this.orderId = orderId;
            this.paymentPrice = paymentPrice;
        }

        public static Pay create(String orderId, Integer paymentPrice) {
            return new Pay(orderId, paymentPrice);
        }

    }

}
