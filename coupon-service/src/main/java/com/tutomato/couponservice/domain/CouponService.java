package com.tutomato.couponservice.domain;

import com.tutomato.couponservice.infrastructure.CouponJpaRepository;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponService {

    private final CouponJpaRepository couponJpaRepository;

    public CouponService(CouponJpaRepository couponJpaRepository) {
        this.couponJpaRepository = couponJpaRepository;
    }

    public void active(String couponId) {
        Coupon coupon = couponJpaRepository.findByCouponId(couponId)
            .orElseThrow(NoResultException::new);

        coupon.active();
    }

}
