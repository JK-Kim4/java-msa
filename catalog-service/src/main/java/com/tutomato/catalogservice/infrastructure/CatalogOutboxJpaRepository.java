package com.tutomato.catalogservice.infrastructure;

import com.tutomato.catalogservice.domain.outbox.CatalogOutbox;
import com.tutomato.commonmessaging.common.OutboxStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CatalogOutboxJpaRepository extends JpaRepository<CatalogOutbox, Long> {

    @Query("""
        select co
        from CatalogOutbox co
        where co.status = :status
        order by co.createdAt desc
        """)
    List<CatalogOutbox> findTop100PendingList(OutboxStatus status, Pageable pageable);
}
