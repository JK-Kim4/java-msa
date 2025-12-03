package com.tutomato.couponservice.infrastructure;

import com.tutomato.couponservice.domain.Coupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCouponId(String couponId);
}
