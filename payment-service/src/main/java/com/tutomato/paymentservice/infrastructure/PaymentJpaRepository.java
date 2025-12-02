package com.tutomato.paymentservice.infrastructure;

import com.tutomato.commonmessaging.common.OutboxStatus;
import com.tutomato.paymentservice.domain.Payment;
import com.tutomato.paymentservice.domain.outbox.PaymentOutbox;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

}
