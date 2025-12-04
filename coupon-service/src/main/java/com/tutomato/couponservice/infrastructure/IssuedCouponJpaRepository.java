package com.tutomato.couponservice.infrastructure;

import com.tutomato.couponservice.domain.IssuedCoupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long> {

    @Query(
        """
                  select ic
                  from IssuedCoupon ic
                  where ic.coupon.couponId = :couponId and ic.ownUserId = :userId
            """
    )
    Optional<IssuedCoupon> findByCouponIdAndOwnUserId(
        @Param("couponId") String couponId,
        @Param("userId") String userId
    );

}
