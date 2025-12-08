package com.tutomato.couponservice.infrastructure.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    @Around("@annotation(com.tutomato.couponservice.infrastructure.lock.DistributedLock)")
    public Object doWithLock(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        DistributedLock lock = getDistributedLockAnnotation(joinPoint);
        String lockKey = createLockKey(joinPoint, lock);

        RLock rLock = redissonClient.getLock(lockKey);

        boolean acquired = false;
        int attempt = 0;
        int maxAttempts = lock.retryCount() + 1; // 최초 1회 + 재시도 N회

        try {
            while (attempt < maxAttempts && !acquired) {
                attempt++;
                try {
                    acquired = rLock.tryLock(
                        lock.waitTime(),
                        lock.leaseTime(),
                        lock.timeUnit()
                    );
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("락 획득 중 인터럽트", e);
                }

                if (!acquired) {
                    logger.warn("[DISTRIBUTED-LOCK-AOP] lock acquire failed. key={}, attempt={}/{}",
                        lockKey, attempt, maxAttempts);

                    if (attempt < maxAttempts) {
                        // 다음 재시도 전 대기
                        Thread.sleep(lock.retryDelay());
                    }
                }
            }

            if (!acquired) {
                // 최종 실패 처리 – 비즈니스 예외로 감싸도 됨
                throw new IllegalStateException("락 획득 실패(재시도 초과): " + lockKey);
            }

            logger.info("[DISTRIBUTED-LOCK-AOP] BEFORE method={}, key={}", method, lockKey);
            return joinPoint.proceed();

        } finally {
            if (acquired && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                logger.info("[DISTRIBUTED-LOCK-AOP] AFTER method={}, key={}", method, lockKey);
            }
        }
    }

    private DistributedLock getDistributedLockAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(DistributedLock.class);
    }

    private String createLockKey(ProceedingJoinPoint joinPoint, DistributedLock lock) {
        // 1. Enum 기반 prefix
        String prefix = lock.key().code();

        // 2. SpEL로 keyValue 평가
        String keyValueExpression = lock.keyValue();
        Object keyValue = evaluateSpel(joinPoint, keyValueExpression);

        // 3. 최종 키 조합
        //    lock:update-stock:123
        return "lock:" + prefix + ":" + keyValue;
    }

    private Object evaluateSpel(ProceedingJoinPoint joinPoint, String expression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String[] paramNames = paramNameDiscoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        return parser.parseExpression(expression).getValue(context);
    }
}
