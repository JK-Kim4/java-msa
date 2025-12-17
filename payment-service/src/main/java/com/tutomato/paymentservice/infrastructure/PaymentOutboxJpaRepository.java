package com.tutomato.paymentservice.infrastructure;

import com.tutomato.commonmessaging.common.OutboxStatus;
import com.tutomato.paymentservice.domain.outbox.PaymentOutbox;
import com.tutomato.paymentservice.interfaces.PaymentOutboxRow;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutbox, Long> {


    @Query("""
        select po
        from PaymentOutbox po
        where po.status = :status
        order by po.createdAt desc
        """)
    List<PaymentOutbox> findPendingOutbox(OutboxStatus status, Pageable pageable);

    @Query("""
        select new com.tutomato.paymentservice.interfaces.PaymentOutboxRow(po.id, po.payload, po.eventType)
        from PaymentOutbox po
        where po.status in :statuses
        order by po.createdAt desc
        """)
    List<PaymentOutboxRow> findClaimTop100(List<OutboxStatus> statuses, Pageable pageable);

    @Modifying
    @Query("""
              update PaymentOutbox po
              set po.status = 'PUBLISHED'
              where po.id = :id
        """)
    void updateStatusToSent(Long id);

    @Modifying
    @Query("""
              update PaymentOutbox po
              set po.status = 'FAILED'
              where po.id = :id
        """)
    void updateStatusToFail(Long outboxId);

}
