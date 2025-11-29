package com.tutomato.catalogservice.domain;

public class DecreaseStockCommand {

    private String  productId;
    private Integer decreaseQuantity;

    protected DecreaseStockCommand() {}

    public DecreaseStockCommand(String productId, Integer decreaseQuantity) {
        this.productId = productId;
        this.decreaseQuantity = decreaseQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getDecreaseQuantity() {
        return decreaseQuantity;
    }
}
