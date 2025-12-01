package com.tutomato.catalogservice.domain;

import com.tutomato.catalogservice.infrastructure.CatalogEntity;
import com.tutomato.catalogservice.infrastructure.CatalogJpaRepository;
import java.util.List;

import com.tutomato.catalogservice.infrastructure.lock.DistributedLock;
import com.tutomato.catalogservice.infrastructure.lock.LockKey;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogService {

    private final CatalogJpaRepository catalogJpaRepository;

    public CatalogService(CatalogJpaRepository catalogJpaRepository) {
        this.catalogJpaRepository = catalogJpaRepository;
    }

    public List<Catalog> findAll() {
        List<CatalogEntity> catalogEntities = catalogJpaRepository.findAll();

        return catalogEntities.stream().map(Catalog::from).toList();
    }

    @DistributedLock(
        key = LockKey.UPDATE_ITEM_STOCK,
        keyValue = "#command.productId",
        retryCount = 5,
        retryDelay = 300
    )
    @Transactional
    public Catalog decreaseStock(DecreaseStockCommand command){
        CatalogEntity entity = catalogJpaRepository.findByProductId(command.getProductId())
                .orElseThrow(() -> new NoResultException("Product not found"));

        entity.decreaseStock(command.getDecreaseQuantity());

        return Catalog.from(entity);
    }

    @Transactional
    public void decreaseStocks(List<DecreaseStockCommand> commands) {
        commands.forEach(this::decreaseStock);
    }
}
