package com.tutomato.catalogservice.domain;

public class DecreaseStockCommand {

    private String productId;
    private Integer decreaseQuantity;

    protected DecreaseStockCommand() {
    }

    protected DecreaseStockCommand(String productId, Integer decreaseQuantity) {
        this.productId = productId;
        this.decreaseQuantity = decreaseQuantity;
    }

    public static DecreaseStockCommand of(String productId, Integer decreaseQuantity) {
        return new DecreaseStockCommand(productId, decreaseQuantity);
    }

    public String getProductId() {
        return productId;
    }

    public Integer getDecreaseQuantity() {
        return decreaseQuantity;
    }
}
