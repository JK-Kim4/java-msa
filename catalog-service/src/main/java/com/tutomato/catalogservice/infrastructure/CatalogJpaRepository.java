package com.tutomato.catalogservice.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface CatalogJpaRepository extends JpaRepository<CatalogEntity, Long> {

    Optional<CatalogEntity> findByProductId(String productId);

}
