package com.tutomato.paymentservice.infrastructure.lock;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IdempotencyKeyGenerator {

    private IdempotencyKeyGenerator() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    // 기존 이벤트 기반
    public static String generateIdempotencyKey(Object... args) {
        if (args == null || Stream.of(args).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Idempotency key를 생성할 때 null 파라미터가 포함될 수 없습니다.");
        }
        String rawKey = Stream.of(args)
            .map(Object::toString)
            .collect(Collectors.joining(":"));
        return hash(rawKey);
    }

    private static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not supported", e);
        }
    }


}
