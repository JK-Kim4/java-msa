package com.tutomato.catalogservice.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class TransactionLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(TransactionLoggingAspect.class);

    @Around("@annotation(org.springframework.transaction.annotation.Transactional) || " +
            "@within(org.springframework.transaction.annotation.Transactional)")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();

        boolean txActiveBefore = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("[TX-AOP] BEFORE method={}, txActive={}", method, txActiveBefore);

        // 이 시점에는 이미 TransactionInterceptor가 트랜잭션을 연 상태이므로,
        // 커밋/롤백 이벤트를 로깅하기 위해 Synchronization 등록
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void beforeCommit(boolean readOnly) {
                    log.info("[TX-AOP] beforeCommit method={}, readOnly={}", method, readOnly);
                }

                @Override
                public void afterCommit() {
                    log.info("[TX-AOP] afterCommit method={}", method);
                }

                @Override
                public void afterCompletion(int status) {
                    String s = switch (status) {
                        case STATUS_COMMITTED -> "COMMITTED";
                        case STATUS_ROLLED_BACK -> "ROLLED_BACK";
                        default -> "UNKNOWN(" + status + ")";
                    };
                    log.info("[TX-AOP] afterCompletion method={}, status={}", method, s);
                }
            });
        }

        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            boolean txActiveAfter = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("[TX-AOP] AFTER method={}, txActive={}", method, txActiveAfter);
        }
    }
}