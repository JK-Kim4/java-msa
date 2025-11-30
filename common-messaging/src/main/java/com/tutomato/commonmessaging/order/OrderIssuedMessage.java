package com.tutomato.commonmessaging.order;

public record OrderIssuedMessage(
    String orderId,
    String productId,
    Integer decreaseQuantity
) {

}
