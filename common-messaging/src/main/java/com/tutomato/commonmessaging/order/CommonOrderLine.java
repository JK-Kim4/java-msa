package com.tutomato.commonmessaging.order;

public record CommonOrderLine(
    String productId,
    Integer unitPrice,
    Integer decreaseQuantity
) {

    public Integer calculate() {
        return unitPrice * decreaseQuantity;
    }

}
