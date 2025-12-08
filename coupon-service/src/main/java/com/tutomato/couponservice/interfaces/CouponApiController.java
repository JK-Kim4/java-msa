package com.tutomato.couponservice.interfaces;

import com.tutomato.couponservice.application.CouponCreateService;
import com.tutomato.couponservice.application.CouponIssueService;
import com.tutomato.couponservice.application.CouponRequestService;
import com.tutomato.couponservice.application.dto.CouponCommand.Issue;
import com.tutomato.couponservice.application.dto.CouponCommand.Request;
import com.tutomato.couponservice.application.dto.CouponResult;
import com.tutomato.couponservice.interfaces.dto.CouponCreateRequest;
import com.tutomato.couponservice.interfaces.dto.CouponCreateResponse;
import java.time.Instant;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponApiController {

    private final Environment environment;
    private final CouponCreateService couponCreateService;
    private final CouponIssueService couponIssueService;
    private final CouponRequestService couponRequestService;

    public CouponApiController(
        Environment environment,
        CouponCreateService couponCreateService,
        CouponIssueService couponIssueService,
        CouponRequestService couponRequestService) {
        this.environment = environment;
        this.couponCreateService = couponCreateService;
        this.couponIssueService = couponIssueService;
        this.couponRequestService = couponRequestService;
    }


    @GetMapping("/health-check")
    public String healthCheck() {
        return String.format(
            "Order service is running on local port %s (server port: %s) \n bus test: %s",
            environment.getProperty("local.server.port"),
            environment.getProperty("local.server.port"),
            environment.getProperty("bus.test"));
    }

    @PostMapping("/coupons")
    public ResponseEntity<CouponCreateResponse> create(
        @RequestBody CouponCreateRequest request
    ) {
        CouponResult.Create result = couponCreateService.create(request.toCommand());

        return ResponseEntity.ok(new CouponCreateResponse(result.getCouponId()));
    }

    @PostMapping("/{couponId}/request")
    public ResponseEntity<Void> request(
        @PathVariable("couponId") String couponId,
        @RequestBody String userId
    ) {
        couponRequestService.request(Request.of(couponId, userId));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/{couponId}/request")
    public ResponseEntity<Void> requestV1(
        @PathVariable("couponId") String couponId,
        @RequestBody String userId
    ) {
        couponIssueService.issueV1(Issue.of(couponId, userId, Instant.now()));

        return ResponseEntity.ok().build();
    }
}
