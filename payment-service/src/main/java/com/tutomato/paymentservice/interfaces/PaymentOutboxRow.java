package com.tutomato.paymentservice.interfaces;

import com.tutomato.commonmessaging.topic.KafkaTopics;

public class PaymentOutboxRow {

    private Long id;
    private String payload;
    private String eventType;

    protected PaymentOutboxRow(Long id) {
        this.id = id;
    }

    public PaymentOutboxRow(Long id, String payload, String eventType) {
        this.id = id;
        this.payload = payload;
        this.eventType = eventType;
    }

    public Long getId() {
        return id;
    }

    public String getPayload() {
        return payload;
    }

    public String getEventType() {
        return eventType;
    }

    public boolean isSuccess() {
        return this.eventType.equalsIgnoreCase(KafkaTopics.PAYMENT_SUCCESS);
    }
}
