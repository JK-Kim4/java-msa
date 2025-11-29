package com.tutomato.catalogservice.domain;

import com.tutomato.catalogservice.infrastructure.CatalogEntity;
import com.tutomato.catalogservice.infrastructure.CatalogJpaRepository;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class CatalogServiceTest {

    @Autowired
    CatalogService catalogService;
    @Autowired
    CatalogJpaRepository catalogJpaRepository;

    private CatalogEntity wirelessMouse = null;

    @Container
    static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:17-alpine")
                    .withDatabaseName("msa-catalog")
                    .withUsername("msa-catalog")
                    .withPassword("msa-catalog");

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @PreDestroy
    public void preDestroy() {
        if (POSTGRES_CONTAINER.isRunning()) {
            System.out.println("Stopping postgresql container");
            POSTGRES_CONTAINER.stop();
        }
    }

    @BeforeEach
    void setUp() {
        CatalogEntity entity = CatalogEntity.create(
                "P-1000",
                "무선 마우스",
                50,
                3000
        );

        wirelessMouse = catalogJpaRepository.save(entity);
    }

    @Test
    void 상품_재고_차감_내역이_데이터베이스에_정상적으로_반영된다() {
        int decreaseAmount = 10;
        int expectedAmount = wirelessMouse.getStock() - decreaseAmount;

        DecreaseStockCommand command = new DecreaseStockCommand(
                wirelessMouse.getProductId(),
                decreaseAmount
        );

        Catalog decreaseResult = catalogService.decreaseStock(command);

        CatalogEntity entity = catalogJpaRepository.findByProductId(wirelessMouse.getProductId()).get();

        assertThat(decreaseResult.getProductId()).isEqualTo(entity.getProductId());
        assertThat(decreaseResult.getStock()).isEqualTo(entity.getStock());
        assertThat(entity.getStock()).isEqualTo(expectedAmount);
    }


}