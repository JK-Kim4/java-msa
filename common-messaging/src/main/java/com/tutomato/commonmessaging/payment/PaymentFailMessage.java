package com.tutomato.commonmessaging.payment;

public record PaymentFailMessage(
    String paymentId,
    String orderId,
    String userId,
    Integer paymentPrice
) {


}
