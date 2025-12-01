package com.tutomato.orderservice.infrastructure.message;

import org.springframework.kafka.core.KafkaOperations;

@FunctionalInterface
public interface KafkaTransactionalAction {

    void doInTransaction(KafkaOperations<String, Object> operations);
}
