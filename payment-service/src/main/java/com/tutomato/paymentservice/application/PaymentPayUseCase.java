package com.tutomato.paymentservice.application;

public interface PaymentPayUseCase {

    PaymentResult.Pay pay(PaymentCommand.Pay command);

}
