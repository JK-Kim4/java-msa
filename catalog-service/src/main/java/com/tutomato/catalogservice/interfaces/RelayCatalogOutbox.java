package com.tutomato.catalogservice.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.catalogservice.domain.outbox.CatalogOutbox;
import com.tutomato.catalogservice.domain.outbox.CatalogOutboxService;
import com.tutomato.catalogservice.infrastructure.message.KafkaMessagePublisher;
import com.tutomato.commonmessaging.catalog.DecreaseStockCompleteMessage;
import com.tutomato.commonmessaging.catalog.DecreaseStockFailMessage;
import java.util.List;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RelayCatalogOutbox {

    private final Logger logger = LoggerFactory.getLogger(RelayCatalogOutbox.class);

    private final ObjectMapper objectMapper;
    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final CatalogOutboxService catalogOutboxService;

    public RelayCatalogOutbox(
        ObjectMapper objectMapper,
        KafkaMessagePublisher kafkaMessagePublisher,
        CatalogOutboxService catalogOutboxService) {
        this.objectMapper = objectMapper;
        this.kafkaMessagePublisher = kafkaMessagePublisher;
        this.catalogOutboxService = catalogOutboxService;
    }

    @Transactional
    @Scheduled(initialDelay = 5000L, fixedDelay = 1000L)
    @SchedulerLock(
        name = "catalogOutboxRelayJob",
        lockAtMostFor = "5s",
        lockAtLeastFor = "1s"
    )
    public void relay() {

        List<CatalogOutbox> outboxes = catalogOutboxService.findTop100PendingList();

        outboxes.forEach(outbox -> {
            try {
                if (outbox.isSuccessMessage()) {
                    DecreaseStockCompleteMessage message
                        = objectMapper.readValue(outbox.getPayload(), DecreaseStockCompleteMessage.class);
                    kafkaMessagePublisher.send(message);
                } else {
                    DecreaseStockFailMessage message
                        = objectMapper.readValue(outbox.getPayload(), DecreaseStockFailMessage.class);
                    kafkaMessagePublisher.fail(message);
                }

                outbox.markPublished();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });


    }

}
