package com.tutomato.catalogservice.domain;

import com.tutomato.catalogservice.infrastructure.CatalogEntity;
import com.tutomato.catalogservice.infrastructure.CatalogJpaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

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
}
