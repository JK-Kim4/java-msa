package com.tutomato.orderservice.infrastructure;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequestV2.OrderLine;
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
public class OrderLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntityV2 orderEntity;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Integer unitPrice;

    protected OrderLineEntity() {
    }

    protected OrderLineEntity(
        OrderEntityV2 entity,
        String productId,
        Integer quantity,
        Integer unitPrice
    ) {
        this.orderEntity = entity;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderLineEntity create(
        OrderEntityV2 entity,
        OrderLine orderLine
    ) {
        return new OrderLineEntity(
            entity,
            orderLine.getProductId(),
            orderLine.getQuantity(),
            orderLine.getUnitPrice()
        );
    }

    public Integer calculateLinePrice() {
        return quantity * unitPrice;
    }

}
