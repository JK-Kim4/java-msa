package com.tutomato.commonmessaging.topic;

public class KafkaTopics {


    protected KafkaTopics() {
        throw new IllegalStateException("External topics class cannot be instantiated");
    }

    public static final String ORDER_COMPLETED = "order.completed";
    public static final String ORDER_COMPLETED_DLQ = "order.completed.dlq";

    public static final String ORDER_PENDING = "order.pending";
    public static final String ORDER_COMPLETE = "order.complete";
    public static final String PAYMENT_SUCCESS = "payment.success";
    public static final String PAYMENT_FAIL = "payment.fail";
    public static final String CATALOG_STOCK_DECREASE = "catalog.stock-decrease";
    public static final String CATALOG_STOCK_DECREASE_FAIL = "catalog.stock-decrease.fail";
    public static final String COUPON_CREATE = "coupon-create";



    public static class TopicGroups {

        protected TopicGroups() {
            throw new IllegalStateException("External topics class cannot be instantiated");
        }

        public static final String ORDER_COMPLETED = "catalog-service.order.completed-group";
        public static final String ORDER_COMPLETE = "order-complete-group";
        public static final String PAYMENT_SUCCESS = "payment-success-group";
        public static final String PAYMENT_FAIL = "payment-fail-group";
        public static final String STOCK_DECREASE_SUCCESS = "catalog-stock-decrease-success-group";
        public static final String STOCK_DECREASE_FAIL = "catalog-stock-decrease-fail-group";;
        public static final String COUPON_CREATE = "coupon-create-group";
    }
}

