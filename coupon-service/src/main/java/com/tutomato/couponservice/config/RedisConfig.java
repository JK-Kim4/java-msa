package com.tutomato.couponservice.config;

import java.time.Duration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        config.setCodec(StringCodec.INSTANCE);

        String address = String.format("redis://%s:%d",
            redisProperties.getHost(),
            redisProperties.getPort()
        );

        SingleServerConfig single = config.useSingleServer()
            .setAddress(address)
            .setDatabase(redisProperties.getDatabase());

        // command timeout
        Duration timeout = redisProperties.getTimeout();
        if (timeout != null) {
            single.setTimeout((int) timeout.toMillis());
        }

        // connect timeout
        Duration connectTimeout = redisProperties.getConnectTimeout();
        if (connectTimeout != null) {
            single.setConnectTimeout((int) connectTimeout.toMillis());
        }

        return Redisson.create(config);
    }

    /**
     * Spring Data Redis가 사용할 ConnectionFactory를 Redisson 기반으로 구성
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);

        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
