package com.tutomato.commonmessaging.payment;

public record PaymentSuccessMessage(
    String paymentId,
    String orderId,
    Integer paymentPrice
) {

}
