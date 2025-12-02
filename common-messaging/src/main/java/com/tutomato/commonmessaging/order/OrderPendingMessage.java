package com.tutomato.commonmessaging.order;

import java.util.List;

public record OrderPendingMessage(
    String orderId,
    List<CommonOrderLine> commonOrderLine
) {

    public Integer calculatePriceSum() {
        return commonOrderLine.stream().mapToInt(CommonOrderLine::calculate).sum();
    }

}
