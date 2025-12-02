package com.tutomato.commonmessaging.order;

public record OrderLine(
    String productId,
    Integer unitPrice,
    Integer decreaseQuantity
) {

    public Integer calculate() {
        return unitPrice * decreaseQuantity;
    }

}
