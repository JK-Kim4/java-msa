package com.tutomato.couponservice.application;

import com.tutomato.couponservice.application.dto.CouponCommand;
import com.tutomato.couponservice.infrastructure.RedisRepository;
import com.tutomato.couponservice.infrastructure.message.KafkaConsumer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponRequestService {

    private final RedisRepository redisRepository;

    public CouponRequestService(
        RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public void request(CouponCommand.Request command) {
        long result = redisRepository.requestCouponAtomic(
            KafkaConsumer.COUPON_COUNTER_PREFIX + command.getCouponId(),
            String.format(KafkaConsumer.COUPON_CANDIDATES_PREFIX, command.getCouponId()),
            command.getUserId(),
            command.getRequestedAt()
        );

        if (result == -1L) {
            throw new IllegalStateException("신청할 수 없는 쿠폰입니다.");
        }
        if (result == -2L) {
            throw new IllegalStateException("쿠폰 수량이 모두 소진되었습니다.");
        }
        if (result == -3L) {
            throw new IllegalStateException("이미 신청한 사용자입니다.");
        }
    }


}
