package com.tutomato.couponservice.infrastructure;

import com.tutomato.couponservice.domain.Coupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCouponId(String couponId);

    @Query("""
              select c.id
              from Coupon c
              where c.isActive = true
        """)
    List<Long> findIssuableCouponIdsBy();
}
