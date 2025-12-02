package com.tutomato.orderservice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_lines")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order orderEntity;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Integer unitPrice;

    protected OrderLine() {
    }

    protected OrderLine(
        Order entity,
        String productId,
        Integer quantity,
        Integer unitPrice
    ) {
        this.orderEntity = entity;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderLine create(
        Order entity,
        CreateOrderRequest.OrderLineDto orderLineDto
    ) {
        return new OrderLine(
            entity,
            orderLineDto.getProductId(),
            orderLineDto.getQuantity(),
            orderLineDto.getUnitPrice()
        );
    }

    public Integer calculateLinePrice() {
        return quantity * unitPrice;
    }

    public Long getId() {
        return id;
    }

    public Order getOrderEntity() {
        return orderEntity;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }
}
