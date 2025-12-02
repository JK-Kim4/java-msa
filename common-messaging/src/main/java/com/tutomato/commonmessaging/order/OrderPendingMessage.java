package com.tutomato.commonmessaging.order;

import java.util.List;

public record OrderPendingMessage(
    String orderId,
    List<OrderLine> orderLine
) {

    public Integer calculatePriceSum() {
        return orderLine.stream().mapToInt(OrderLine::calculate).sum();
    }

}
