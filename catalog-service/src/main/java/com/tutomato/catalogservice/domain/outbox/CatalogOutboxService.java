package com.tutomato.catalogservice.domain.outbox;

import com.tutomato.catalogservice.infrastructure.CatalogOutboxJpaRepository;
import com.tutomato.commonmessaging.common.OutboxStatus;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CatalogOutboxService {

    private final CatalogOutboxJpaRepository catalogOutboxJpaRepository;

    public CatalogOutboxService(CatalogOutboxJpaRepository catalogOutboxJpaRepository) {
        this.catalogOutboxJpaRepository = catalogOutboxJpaRepository;
    }

    public CatalogOutbox save(CatalogOutbox catalogOutbox) {
        return catalogOutboxJpaRepository.save(catalogOutbox);
    }

    public List<CatalogOutbox> findTop100PendingList() {
        Pageable pageable = PageRequest.of(0, 100);
        return catalogOutboxJpaRepository.findTop100PendingList(OutboxStatus.PENDING, pageable);
    }

}
