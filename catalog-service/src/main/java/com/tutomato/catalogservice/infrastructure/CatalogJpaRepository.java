package com.tutomato.catalogservice.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogJpaRepository extends JpaRepository<CatalogEntity, Long> {

    CatalogEntity findByProductId(String productId);

}
