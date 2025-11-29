package com.tutomato.catalogservice.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "catalogs")
public class CatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private String productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    protected CatalogEntity() {
    }

    protected CatalogEntity(String productId, String productName, Integer stock, Integer unitPrice, Instant createdAt) {
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.createdAt = createdAt;
    }

    public static CatalogEntity create(
            String productId,
            String productName,
            Integer stock,
            Integer unitPrice
    ) {
        return new CatalogEntity(productId, productName, stock, unitPrice, Instant.now());
    }

    public void decreaseStock(int decreaseQuantity) {

        if (this.stock < decreaseQuantity) {
            throw new IllegalArgumentException("Stock exceeds stock limit");
        }

        this.stock -= decreaseQuantity;
    }

    public Long getId() {
        return id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }
}
