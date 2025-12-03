package com.tutomato.orderservice.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.tutomato.orderservice.infrastructure.OrderRepository;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class OrderServiceRetrySuccessTest {

    @Autowired
    OrderService orderService;

    @MockitoBean
    OrderRepository orderRepository;


    @Container
    static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
        new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("msa-order")
            .withUsername("msa-order")
            .withPassword("msa-order");

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


    @Test
    void decreaseStockComplete_모든_재시도_실패시_예외발생() {
        // given
        String orderId = "ORDER-2";

        given(orderRepository.findByOrderIdWithPessimisticLock(orderId))
            .willThrow(new PessimisticLockingFailureException("lock fail"));

        // when & then
        assertThatThrownBy(() -> orderService.decreaseStockComplete(orderId))
            .isInstanceOf(PessimisticLockingFailureException.class);

        // maxAttempts = 5 이므로 총 5번 호출
        then(orderRepository)
            .should(times(5))
            .findByOrderIdWithPessimisticLock(orderId);
    }

}