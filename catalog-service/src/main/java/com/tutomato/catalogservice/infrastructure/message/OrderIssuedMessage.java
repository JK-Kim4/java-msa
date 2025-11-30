package com.tutomato.catalogservice.infrastructure.message;

public record OrderIssuedMessage(
    String orderId,
    String productId,
    Integer decreaseQuantity
) {

}