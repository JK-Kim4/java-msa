package com.tutomato.couponservice.application;

import com.tutomato.commonmessaging.coupon.CouponCreateMessage;
import com.tutomato.couponservice.application.dto.CouponCommand;
import com.tutomato.couponservice.application.dto.CouponResult;
import com.tutomato.couponservice.domain.Coupon;
import com.tutomato.couponservice.infrastructure.CouponJpaRepository;
import com.tutomato.couponservice.infrastructure.message.KafkaMessagePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponCreateService {


    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final CouponJpaRepository couponJpaRepository;

    public CouponCreateService(
        KafkaMessagePublisher kafkaMessagePublisher,
        CouponJpaRepository couponJpaRepository
    ) {
        this.kafkaMessagePublisher = kafkaMessagePublisher;
        this.couponJpaRepository = couponJpaRepository;
    }

    public CouponResult.Create create(CouponCommand.Create command) {

        Coupon coupon = couponJpaRepository.save(command.toEntity());

        CouponCreateMessage message = CouponCreateMessage.of(
            coupon.getCouponId(),
            coupon.getTotalAmount(),
            coupon.getExpiredAt()
        );

        kafkaMessagePublisher.send(message);

        return CouponResult.Create.from(coupon);
    }

}
