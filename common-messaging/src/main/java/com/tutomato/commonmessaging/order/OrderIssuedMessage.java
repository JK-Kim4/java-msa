package com.tutomato.commonmessaging.order;

import java.util.List;

public record OrderIssuedMessage(
    String orderId,
    List<CommonOrderLine> commonOrderLine
) {

}
