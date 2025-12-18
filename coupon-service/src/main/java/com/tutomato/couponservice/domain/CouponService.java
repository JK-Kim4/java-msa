package com.tutomato.couponservice.domain;

import com.tutomato.couponservice.infrastructure.CouponJpaRepository;
import jakarta.persistence.NoResultException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponService {

    private final CouponJpaRepository couponJpaRepository;

    public CouponService(CouponJpaRepository couponJpaRepository) {
        this.couponJpaRepository = couponJpaRepository;
    }

    public Coupon save(Coupon entity) {
        return couponJpaRepository.save(entity);
    }

    public List<Long> findIssuableCouponIds() {
        return couponJpaRepository.findIssuableCouponIds();
    }

    public void active(String couponId) {
        Coupon coupon = couponJpaRepository.findByCouponId(couponId)
            .orElseThrow(NoResultException::new);

        coupon.active();
    }

}
