package com.tutomato.orderservice.infrastructure;

import com.tutomato.commonmessaging.common.OutboxStatus;
import com.tutomato.orderservice.infrastructure.message.OrderOutbox;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long> {

    @Query("""
            select ob
            from OrderOutbox ob
            where ob.status = :status
            order by ob.createdAt desc
        """)
    List<OrderOutbox> findPendingOutbox(@Param("status") OutboxStatus status, Pageable pageable);

}
