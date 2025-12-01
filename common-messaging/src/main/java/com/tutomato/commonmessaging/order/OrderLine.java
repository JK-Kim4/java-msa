package com.tutomato.commonmessaging.order;

public record OrderLine(
    String productId,
    Integer decreaseQuantity
) {

}
