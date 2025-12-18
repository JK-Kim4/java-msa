package com.tutomato.orderservice.application;

import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.domain.dto.OrderCommand;
import com.tutomato.orderservice.infrastructure.OrderRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class PlaceOrderServiceTest {


    @Autowired
    PlaceOrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @MockitoBean
    KafkaTemplate<String, String> kafkaTemplate; // 서비스 시그니처와 동일


    @Test
    void dbCommit_success_but_kafka_send_fails_after_commit() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> sendError = new AtomicReference<>();

        Mockito.doAnswer(inv -> {
            CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();

            executor.submit(() -> {
                try {
                    // "커밋 이후" 실패를 만들기 위한 인위적 지연
                    Thread.sleep(200);

                    RuntimeException ex = new RuntimeException("Simulated broker/network failure");
                    sendError.set(ex);
                    future.completeExceptionally(ex);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    RuntimeException ex = new RuntimeException(
                        "Interrupted while simulating send failure", e);
                    sendError.set(ex);
                    future.completeExceptionally(ex);
                } finally {
                    latch.countDown();
                }
            });

            return future;
        }).when(kafkaTemplate).send(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        try {
            // when: 이 시점에 트랜잭션 종료 + 커밋 완료
            Order order = orderService.placeOrder(createCommand());

            // then: DB는 커밋돼서 존재해야 함
            Assertions.assertTrue(orderRepository.existsById(order.getOrderId()));

            // then: 커밋 이후 Kafka 전송 실패가 관측되어야 함
            Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
            Assertions.assertNotNull(sendError.get());
        } finally {
            executor.shutdownNow();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    private OrderCommand.Create createCommand() {
        return OrderCommand.Create.of(
            "testUser",
            List.of()
        );
    }

}