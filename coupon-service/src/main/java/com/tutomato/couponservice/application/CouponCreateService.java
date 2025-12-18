package com.tutomato.couponservice.application;

import com.tutomato.commonmessaging.coupon.CouponCreateMessage;
import com.tutomato.couponservice.application.dto.CouponCommand;
import com.tutomato.couponservice.application.dto.CouponResult;
import com.tutomato.couponservice.domain.Coupon;
import com.tutomato.couponservice.domain.CouponService;
import com.tutomato.couponservice.infrastructure.message.KafkaMessagePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponCreateService {


    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final CouponService couponService;

    public CouponCreateService(
        KafkaMessagePublisher kafkaMessagePublisher,
        CouponService couponService
    ) {
        this.kafkaMessagePublisher = kafkaMessagePublisher;
        this.couponService = couponService;
    }

    public CouponResult.Create create(CouponCommand.Create command) {

        Coupon coupon = couponService.save(command.toEntity());

        CouponCreateMessage message = CouponCreateMessage.of(
            coupon.getCouponId(),
            coupon.getTotalAmount(),
            coupon.getExpiredAt()
        );

        kafkaMessagePublisher.send(message);

        return CouponResult.Create.from(coupon);
    }

}
