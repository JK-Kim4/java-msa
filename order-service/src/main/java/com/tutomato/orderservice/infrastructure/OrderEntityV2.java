package com.tutomato.orderservice.infrastructure;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tutomato.orderservice.interfaces.dto.CreateOrderRequestV2.OrderLine;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class OrderEntityV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @OneToMany(
        mappedBy = "orderEntity",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderLineEntity> orderLines;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    protected OrderEntityV2() {
    }

    protected OrderEntityV2(String userId, String orderId) {
        this.userId = userId;
        this.orderId = orderId;
        this.totalPrice = 0;
        this.orderLines = new ArrayList<>();
    }

    public static OrderEntityV2 from(String userId, String orderId) {
        return new OrderEntityV2(userId, orderId);
    }

    public void allocateOrderLines(List<OrderLineEntity> orderLines) {
        this.orderLines = orderLines;
        this.totalPrice = orderLines.stream()
            .mapToInt(OrderLineEntity::calculateLinePrice)
            .sum();
    }
}
