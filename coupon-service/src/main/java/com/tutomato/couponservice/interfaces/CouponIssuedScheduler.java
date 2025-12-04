package com.tutomato.couponservice.interfaces;

import com.tutomato.couponservice.application.CouponIssueService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CouponIssuedScheduler {

    private final CouponIssueService couponIssueService;

    public CouponIssuedScheduler(CouponIssueService couponIssueService) {
        this.couponIssueService = couponIssueService;
    }

    @Transactional
    @Scheduled(initialDelay = 5000L, fixedDelay = 1000L)
    @SchedulerLock(
        name = "couponIssueJob",
        lockAtMostFor = "5s",
        lockAtLeastFor = "1s"
    )
    public void issue() {
        couponIssueService.issue();
    }
}
