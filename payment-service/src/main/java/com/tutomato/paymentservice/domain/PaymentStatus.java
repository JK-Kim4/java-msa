package com.tutomato.paymentservice.domain;

public enum PaymentStatus {
    CREATE,
    SUCCESS,   // 결제 성공
    PENDING,   // 결제 보류
    FAILED,    // 결제 실패
    CANCELED   // 결제 취소
}
