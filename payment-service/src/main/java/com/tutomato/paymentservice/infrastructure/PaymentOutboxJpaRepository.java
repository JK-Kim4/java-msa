package com.tutomato.paymentservice.infrastructure;

import com.tutomato.commonmessaging.common.OutboxStatus;
import com.tutomato.paymentservice.domain.outbox.PaymentOutbox;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutbox, Long> {


    @Query("""
        select po
        from PaymentOutbox po
        where po.status = :status
        order by po.createdAt desc
        """)
    List<PaymentOutbox> findPendingOutbox(OutboxStatus status, Pageable pageable);
}
