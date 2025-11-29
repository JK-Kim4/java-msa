package com.tutomato.catalogservice;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class RedisIntegrationTest {

    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:7-alpine")
                    .withExposedPorts(6379);

    // Spring Boot의 spring.data.redis.* 설정을
    // 컨테이너 정보로 덮어쓰기
    @DynamicPropertySource
    static void overrideRedisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void shouldSaveAndGetValueFromRedis() {
        // given
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String key = "user:1:name";
        String value = "jongwan";

        // when
        ops.set(key, value);
        Object result = ops.get(key);

        // then
        assertThat(result).isEqualTo(value);
    }
}
