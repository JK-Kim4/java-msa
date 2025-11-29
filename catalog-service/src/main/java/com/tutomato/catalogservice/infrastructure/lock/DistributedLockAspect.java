package com.tutomato.catalogservice.infrastructure.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(lock)")
    public Object doWithLock(
            ProceedingJoinPoint joinPoint,
            DistributedLock lock
    ) throws Throwable {

        String lockKey = createLockKey(joinPoint, lock);
        RLock rLock = redissonClient.getLock(lockKey);

        boolean acquired = false;
        try {
            acquired = rLock.tryLock(
                    lock.waitTime(),
                    lock.leaseTime(),
                    lock.timeUnit()
            );
            if (!acquired) {
                throw new IllegalStateException("락 획득 실패: " + lockKey);
            }

            return joinPoint.proceed();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락 획득 중 인터럽트", e);

        } finally {
            if (acquired && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
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
