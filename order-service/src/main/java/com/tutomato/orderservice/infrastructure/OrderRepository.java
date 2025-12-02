package com.tutomato.orderservice.infrastructure;

import com.tutomato.orderservice.domain.OrderV2;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderV2JpaRepository orderV2JpaRepository;
    private final OrderLineJpaRepository orderLineJpaRepository;

    public OrderRepository(
        OrderV2JpaRepository orderV2JpaRepository,
        OrderLineJpaRepository orderLineJpaRepository
    ) {
        this.orderV2JpaRepository = orderV2JpaRepository;
        this.orderLineJpaRepository = orderLineJpaRepository;
    }

    public OrderEntityV2 save(OrderV2 order) {
        //order 저장
        OrderEntityV2 orderEntityV2 = new OrderEntityV2(order.getUserId(), order.getOrderId());
        orderV2JpaRepository.save(orderEntityV2);

        //order line 저장
        List<OrderLineEntity> orderLines = order.getOrderLines().stream()
            .map(line -> OrderLineEntity.create(orderEntityV2, line))
            .toList();
        orderLineJpaRepository.saveAll(orderLines);

        orderEntityV2.allocateOrderLines(orderLines);

        return orderEntityV2;
    }

}
