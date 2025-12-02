package com.tutomato.paymentservice.infrastructure.lock;

import com.tutomato.commonmessaging.topic.KafkaTopics;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository {


    private static final String ORDER_COMPLETED_IDEMPOTENCY_KEY = "order.complete:Idempotency";

    private final RedissonClient redissonClient;

    public RedisRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 멱등성 키를 Redis Set 에 저장 Redis Key: "order.complete:Idempotency" Value: 실제 멱등성 키 문자열
     */
    public void saveIdempotencyKey(Object message) {
        String idempotencyKey = IdempotencyKeyGenerator.generateIdempotencyKey(message);
        RSet<String> idempotencySet = redissonClient.getSet(ORDER_COMPLETED_IDEMPOTENCY_KEY);
        idempotencySet.add(idempotencyKey);
    }

    /**
     * 이미 처리된 멱등성 키인지 여부 확인
     */
    public boolean hasIdempotencyKey(Object message) {
        String idempotencyKey = IdempotencyKeyGenerator.generateIdempotencyKey(message);
        RSet<String> idempotencySet = redissonClient.getSet(ORDER_COMPLETED_IDEMPOTENCY_KEY);
        return idempotencySet.contains(idempotencyKey);
    }

}
