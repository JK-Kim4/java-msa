package com.tutomato.orderservice.application;

import com.tutomato.orderservice.domain.Order;
import com.tutomato.orderservice.domain.dto.OrderCommand;
import com.tutomato.orderservice.infrastructure.OrderRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
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
        var scheduler = Executors.newSingleThreadScheduledExecutor();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> sendError = new AtomicReference<>();

        Mockito.doAnswer(inv -> {
            CompletableFuture<SendResult<String, Object>> f = new CompletableFuture<>();
            // "커밋 이후"를 만들기 위해 지연 후 실패 처리
            scheduler.schedule(() -> {
                RuntimeException ex = new RuntimeException("Simulated broker/network failure");
                sendError.set(ex);
                f.completeExceptionally(ex);
                latch.countDown();
            }, 200, TimeUnit.MILLISECONDS);
            return f;
        }).when(kafkaTemplate).send(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        // when
        Order order = orderService.placeOrder(createCommand()); // 이 시점에 트랜잭션 종료 + 커밋 완료

        // then: DB는 커밋돼서 존재해야 함
        Assertions.assertTrue(orderRepository.existsById(order.getOrderId()));

        // then: 커밋 이후 Kafka 전송 실패가 관측되어야 함
        Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
        Assertions.assertNotNull(sendError.get());

        scheduler.shutdownNow();
    }

    private OrderCommand.Create createCommand() {
        return OrderCommand.Create.of(
            "testUser",
            List.of()
        );
    }

}