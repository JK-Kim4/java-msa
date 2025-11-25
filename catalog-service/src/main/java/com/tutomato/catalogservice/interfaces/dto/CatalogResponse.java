package com.tutomato.catalogservice.interfaces.dto;

import com.tutomato.catalogservice.domain.Catalog;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogResponse {

    String productId;
    String productName;
    Integer stock;
    Integer unitPrice;
    LocalDateTime createdAt;

    protected CatalogResponse() {
    }

    protected CatalogResponse(String productId, String productName, Integer stock,
        Integer unitPrice, LocalDateTime createdAt) {
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.createdAt = createdAt;
    }

    public static CatalogResponse from(Catalog catalog) {
        return new CatalogResponse(
            catalog.getProductId(),
            catalog.getProductName(),
            catalog.getStock(),
            catalog.getUnitPrice(),
            catalog.getCreatedAt()
        );
    }

    public static List<CatalogResponse> fromList(List<Catalog> catalogs) {
        return catalogs.stream().map(CatalogResponse::from).collect(Collectors.toList());
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
