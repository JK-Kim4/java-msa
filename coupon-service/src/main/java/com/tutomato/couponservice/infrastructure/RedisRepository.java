package com.tutomato.couponservice.infrastructure;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
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

    public boolean existsInZSet(String key, String value) {
        RScoredSortedSet<String> set = getSet(key);
        return set.contains(value);
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

    /**
     * @return 0  = 성공
     *        -1  = 잘못된 쿠폰
     *        -2  = 수량 소진
     *        -3  = 이미 신청한 사용자
     */
    public long requestCouponAtomic(
        String counterKey,
        String candidatesZSetKey,
        String userId,
        Instant requestedAt
    ) {
        long epochMillis = requestedAt.toEpochMilli();

        RScript script = redissonClient.getScript(StringCodec.INSTANCE);

        List<Object> keys = Arrays.asList(counterKey, candidatesZSetKey);

        Long result = script.eval(
            RScript.Mode.READ_WRITE,          // 쓰기 연산이 있으므로 READ_WRITE
            REQUEST_COUPON_LUA,            // Lua 스크립트
            RScript.ReturnType.INTEGER,       // return type: long
            keys,
            userId,                           // ARGV[1]
            String.valueOf(epochMillis)       // ARGV[2]
        );

        return result;
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

    private static final String REQUEST_COUPON_LUA =
        """
        -- KEYS[1] = counter key
        -- KEYS[2] = candidates zset key
        -- ARGV[1] = userId
        -- ARGV[2] = score(epochMillis)
        
        local counter = redis.call('GET', KEYS[1])
        if not counter then
          return -1   -- 쿠폰 없음
        end
        
        counter = tonumber(counter)
        if counter <= 0 then
          return -2   -- 수량 소진
        end
        
        local exists = redis.call('ZSCORE', KEYS[2], ARGV[1])
        if exists then
          return -3   -- 이미 신청한 사용자
        end
        
        redis.call('DECR', KEYS[1])
        redis.call('ZADD', KEYS[2], ARGV[2], ARGV[1])
        return 0       -- 성공
        """;
}
