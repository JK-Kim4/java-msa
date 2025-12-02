package com.tutomato.catalogservice.application;

import com.tutomato.catalogservice.domain.Catalog;
import com.tutomato.catalogservice.domain.DecreaseStockCommand;
import com.tutomato.catalogservice.infrastructure.CatalogEntity;
import com.tutomato.catalogservice.infrastructure.CatalogJpaRepository;
import com.tutomato.catalogservice.infrastructure.lock.DistributedLock;
import com.tutomato.catalogservice.infrastructure.lock.LockKey;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DecreaseStockService {

    private final CatalogJpaRepository catalogJpaRepository;

    public DecreaseStockService(CatalogJpaRepository catalogJpaRepository) {
        this.catalogJpaRepository = catalogJpaRepository;
    }

    @DistributedLock(
        key = LockKey.UPDATE_ITEM_STOCK,
        keyValue = "#command.productId",
        retryCount = 5,
        retryDelay = 300
    )
    public Catalog decreaseStock(DecreaseStockCommand command) {
        CatalogEntity entity = catalogJpaRepository.findByProductId(command.getProductId())
            .orElseThrow(() -> new NoResultException("Product not found"));

        entity.decreaseStock(command.getDecreaseQuantity());

        return Catalog.from(entity);
    }

}
