package com.tutomato.couponservice.infrastructure;

import java.time.Instant;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository {

    private final RedissonClient redissonClient;

    public RedisRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void addValueToZSetWithTimeMilli(
        String key,
        Instant occurredAt,
        String value
    ) {
        RScoredSortedSet<String> set = getSet(key);

        long epochMillis = occurredAt.toEpochMilli();
        double score = (double) epochMillis;

        set.add(score, value);
    }

    public java.util.Collection<String> findBottomN(int n, String key) {
        return rankingSet(key).valueRange(0, n - 1);
    }

    public void setInitial(String key, int initialValue) {
        getAtomicCounter(key).set(initialValue);
    }

    public void setIfNotExistOrThrow(String key, int value) {
        RAtomicLong counter = getAtomicCounter(key);

        if (counter.isExists()) {
            // 이미 초기화된 상태라고 보고 예외
            throw new IllegalStateException("Counter already initialized. key=" + key);
        }

        counter.set(value);
    }

    public int getOrSetInitial(String key, int initialValue) {
        RAtomicLong counter = getAtomicCounter(key);
        if (!counter.isExists()) {
            counter.set(initialValue);
            return initialValue;
        }
        long value = counter.get();
        return safeToInt(value);
    }

    public Integer get(String key) {
        RAtomicLong counter = getAtomicCounter(key);
        if (!counter.isExists()) {
            return null;
        }
        long value = counter.get();
        return safeToInt(value);
    }

    public int increment(String key) {
        long value = getAtomicCounter(key).incrementAndGet();
        return safeToInt(value);
    }

    public int decrement(String key) {
        long value = getAtomicCounter(key).decrementAndGet();
        return safeToInt(value);
    }

    public void delete(String key) {
        getAtomicCounter(key).delete();
    }

    private RScoredSortedSet<String> rankingSet(String key) {
        return redissonClient.getScoredSortedSet(key);
    }

    private RScoredSortedSet<String> getSet(String key) {
        return redissonClient.getScoredSortedSet(key);
    }

    private RAtomicLong getAtomicCounter(String key) {
        return redissonClient.getAtomicLong(key);
    }

    private int safeToInt(long value) {
        if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
            throw new IllegalStateException("counter value overflow: " + value);
        }
        return (int) value;
    }
}
