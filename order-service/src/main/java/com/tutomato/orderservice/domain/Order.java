package com.tutomato.orderservice.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "orders_v2")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @OneToMany(
        mappedBy = "orderEntity",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderLine> orderLines;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    protected Order() {
    }

    protected Order(String userId, String orderId, OrderStatus orderStatus) {
        this.userId = userId;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.totalPrice = 0;
        this.orderLines = new ArrayList<>();
        this.createdAt = Instant.now();
    }

    public static Order create(String userId, String orderId) {
        return new Order(userId, orderId, OrderStatus.CREATED);
    }

    public void allocateOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
        this.totalPrice = orderLines.stream()
            .mapToInt(OrderLine::calculateLinePrice)
            .sum();
    }

    public Long getId() {
        return id;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
