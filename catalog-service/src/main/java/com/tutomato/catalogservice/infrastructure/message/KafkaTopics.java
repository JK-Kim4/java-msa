package com.tutomato.catalogservice.infrastructure.message;

public class KafkaTopics {


    protected KafkaTopics() {
        throw new IllegalStateException("External topics class cannot be instantiated");
    }

    public static final String ORDER_COMPLETED = "order.completed";
    public static final String ORDER_COMPLETED_DLQ = "order.completed.dlq";


    public static class TopicGroups {

        protected TopicGroups() {
            throw new IllegalStateException("External topics class cannot be instantiated");
        }

        public static final String ORDER_COMPLETED = "catalog-service.order.completed-group";
    }
}
