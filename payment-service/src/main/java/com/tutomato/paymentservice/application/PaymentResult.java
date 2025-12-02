package com.tutomato.paymentservice.application;

import com.tutomato.paymentservice.domain.Payment;
import com.tutomato.paymentservice.domain.PaymentStatus;
import java.time.Instant;

public class PaymentResult {

    public static class Pay {

        String paymentId;
        String orderId;
        Integer paymentPrice;
        PaymentStatus paymentStatus;
        Instant paidAt;

        protected Pay() {
        }

        protected Pay(String paymentId, String orderId, Integer paymentPrice,
            PaymentStatus paymentStatus, Instant paidAt) {
            this.paymentId = paymentId;
            this.orderId = orderId;
            this.paymentPrice = paymentPrice;
            this.paymentStatus = paymentStatus;
            this.paidAt = paidAt;
        }

        public static Pay from(Payment payment) {
            return new Pay(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getPaymentPrice(),
                payment.getPaymentStatus(),
                payment.getPaidAt()
            );
        }


    }

}
