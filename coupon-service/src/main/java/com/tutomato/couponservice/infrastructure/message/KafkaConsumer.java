package com.tutomato.couponservice.infrastructure.message;

import com.tutomato.commonmessaging.coupon.CouponCreateMessage;
import com.tutomato.commonmessaging.topic.KafkaTopics;
import com.tutomato.commonmessaging.topic.KafkaTopics.TopicGroups;
import com.tutomato.couponservice.domain.CouponService;
import com.tutomato.couponservice.infrastructure.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final static String COUPON_COUNTER_PREFIX = "coupon:counter:";
    private final static String COUPON_CANDIDATES_PREFIX = "coupon:candidates:";
    private final static String COUPON_WINNERS_PREFIX = "coupon:winners:";

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final CouponService couponService;
    private final RedisRepository redisRepository;

    public KafkaConsumer(RedisRepository redisRepository, CouponService couponService) {
        this.redisRepository = redisRepository;
        this.couponService = couponService;
    }

    @KafkaListener(
        topics = KafkaTopics.COUPON_CREATE,
        groupId = "coupon:" + TopicGroups.COUPON_CREATE
    )
    public void consume(CouponCreateMessage message) {
        redisRepository.setIfNotExistOrThrow(getCouponCounterKey(message.couponId()),
            message.amount());
    }

    private String getCouponCounterKey(String couponId) {
        return COUPON_COUNTER_PREFIX + couponId;
    }
}
