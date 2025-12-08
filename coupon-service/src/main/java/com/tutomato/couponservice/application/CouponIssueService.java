package com.tutomato.couponservice.application;

import com.tutomato.couponservice.application.dto.CouponCommand;
import com.tutomato.couponservice.domain.Coupon;
import com.tutomato.couponservice.domain.IssuedCoupon;
import com.tutomato.couponservice.infrastructure.CouponJpaRepository;
import com.tutomato.couponservice.infrastructure.IssuedCouponJpaRepository;
import com.tutomato.couponservice.infrastructure.RedisRepository;
import com.tutomato.couponservice.infrastructure.lock.DistributedLock;
import com.tutomato.couponservice.infrastructure.lock.LockKey;
import com.tutomato.couponservice.infrastructure.message.KafkaConsumer;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponIssueService {

    private final CouponJpaRepository couponJpaRepository;
    private final IssuedCouponJpaRepository issuedCouponJpaRepository;
    private final RedisRepository redisRepository;

    public CouponIssueService(
        CouponJpaRepository couponJpaRepository,
        IssuedCouponJpaRepository issuedCouponJpaRepository,
        RedisRepository redisRepository
    ) {
        this.redisRepository = redisRepository;
        this.couponJpaRepository = couponJpaRepository;
        this.issuedCouponJpaRepository = issuedCouponJpaRepository;
    }


    @DistributedLock(
        key = LockKey.UPDATE_ITEM_STOCK,
        keyValue = "#command.couponId",
        retryCount = 5,
        retryDelay = 300
    )
    public void issueV1(CouponCommand.Issue command) {
        Integer issuedCouponCount = issuedCouponJpaRepository.getCountByCouponId(
            command.getCouponId());
        Coupon coupon = couponJpaRepository.findByCouponId(command.getCouponId())
            .orElseThrow();

        if (!coupon.isIssuable(issuedCouponCount, command.getRequestedAt())) {
            throw new IllegalStateException(
                "Coupon unissuable for coupon id " + command.getCouponId());
        }

        boolean alreadyIssued = issuedCouponJpaRepository
            .findByCouponIdAndOwnUserId(coupon.getCouponId(), command.getUserId())
            .isPresent();

        if (alreadyIssued) {
            throw new IllegalStateException(
                "Coupon already issued to user  " + command.getUserId());
        }

        IssuedCoupon issuedCoupon = IssuedCoupon.issue(
            coupon,
            command.getUserId(),
            command.getRequestedAt()
        );

        issuedCouponJpaRepository.save(issuedCoupon);
    }

    public void issue() {
        List<Long> issuableCoupons = couponJpaRepository.findIssuableCouponIdsBy();

        for (Long issuableCouponId : issuableCoupons) {
            Coupon coupon = couponJpaRepository.findById(issuableCouponId)
                .orElseThrow();

            String requestedCouponKey =
                String.format(KafkaConsumer.COUPON_CANDIDATES_PREFIX, coupon.getCouponId());

            int issuableCount = calculateIssuableCouponCount(requestedCouponKey,
                coupon.getTotalAmount());

            Collection<String> candidateIds = redisRepository.findBottomN(
                issuableCount,
                requestedCouponKey
            );

            for (String candidateId : candidateIds) {
                boolean alreadyIssued = issuedCouponJpaRepository
                    .findByCouponIdAndOwnUserId(coupon.getCouponId(), candidateId)
                    .isPresent();

                if (alreadyIssued) {
                    continue;
                }

                IssuedCoupon issuedCoupon = IssuedCoupon.issue(
                    coupon,
                    candidateId,
                    Instant.now()
                );

                issuedCouponJpaRepository.save(issuedCoupon);

                redisRepository.addValueToZSetWithTimeMilli(
                    String.format(KafkaConsumer.COUPON_WINNERS_PREFIX, coupon.getCouponId()),
                    Instant.now(),
                    candidateId
                );
            }
        }

    }

    private int calculateIssuableCouponCount(String requestedCouponKey, int totalAmount) {
        int issuedCount = redisRepository.getZSetSize(requestedCouponKey);

        return totalAmount - issuedCount;
    }

}
