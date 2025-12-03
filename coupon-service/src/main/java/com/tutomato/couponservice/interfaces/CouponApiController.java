package com.tutomato.couponservice.interfaces;

import com.tutomato.couponservice.application.CouponCreateService;
import com.tutomato.couponservice.application.dto.CouponResult;
import com.tutomato.couponservice.interfaces.dto.CouponCreateRequest;
import com.tutomato.couponservice.interfaces.dto.CouponCreateResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponApiController {

    private final Environment environment;
    private final CouponCreateService couponCreateService;

    public CouponApiController(
        Environment environment,
        CouponCreateService couponCreateService
    ) {
        this.environment = environment;
        this.couponCreateService = couponCreateService;
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

}
