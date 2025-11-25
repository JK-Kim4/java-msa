package com.tutomato.catalogservice.domain;

import com.tutomato.catalogservice.infrastructure.CatalogEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Catalog {

    String productId;
    String productName;
    Integer stock;
    Integer unitPrice;
    LocalDateTime createdAt;

    protected Catalog() {
    }

    protected Catalog(String productId, String productName, Integer stock, Integer unitPrice,
        LocalDateTime createdAt) {
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.createdAt = createdAt;
    }

    public static Catalog from(CatalogEntity catalogEntity) {
        return new Catalog(
            catalogEntity.getProductId(),
            catalogEntity.getProductName(),
            catalogEntity.getStock(),
            catalogEntity.getUnitPrice(),
            LocalDateTime.ofInstant(catalogEntity.getCreatedAt(), ZoneId.of("Asia/Seoul"))
        );
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
