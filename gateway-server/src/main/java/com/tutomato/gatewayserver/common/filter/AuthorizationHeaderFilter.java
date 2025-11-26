package com.tutomato.gatewayserver.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutomato.gatewayserver.authentication.TokenProvider;
import com.tutomato.gatewayserver.authentication.TokenType;

import com.tutomato.gatewayserver.common.ErrorResponse;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter
    extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(AuthorizationHeaderFilter.class);

    public AuthorizationHeaderFilter(TokenProvider tokenProvider, ObjectMapper objectMapper) {
        super(Config.class);
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(final AuthorizationHeaderFilter.Config config) {
        return (exchange, chain) -> {
            final ServerHttpRequest request = exchange.getRequest();

            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader == null) {
                return onError(exchange,
                    "AUTH-001",
                    "인증을 위한 Authorization 헤더가 존재하지 않습니다.",
                    HttpStatus.FORBIDDEN
                );
            }

            if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
                return onError(exchange,
                    "AUTH-002",
                    "유효하지 않은 Authorization 헤더 형식입니다. 'Bearer {accessToken}' 형식이어야 합니다.",
                    HttpStatus.FORBIDDEN
                );
            }

            String accessToken = authorizationHeader.substring(BEARER_PREFIX.length()).trim();

            if (!StringUtils.hasText(accessToken)) {
                return onError(exchange,
                    "AUTH-003",
                    "인증을 위한 access token 이 비어 있습니다.",
                    HttpStatus.FORBIDDEN
                );
            }

            try {
                tokenProvider.validateTokenOrThrow(accessToken, TokenType.ACCESS);
            } catch (Exception e) {
                logger.error("토큰 검증 실패: {}", e.getMessage());
                return onError(exchange,
                    "AUTH-004",
                    e.getMessage(),
                    HttpStatus.FORBIDDEN
                );
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange,
        String errorCode,
        String message,
        HttpStatus status) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String path = exchange.getRequest().getURI().getPath();
        ErrorResponse errorResponse = ErrorResponse.of(status, errorCode, message, path);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(errorResponse);
        } catch (JsonProcessingException ex) {
            // JSON 변환 실패 시 최소한의 정보라도 문자열로 내려주기
            String fallback = """
                {"status":%d,"code":"%s","message":"%s"}
                """.formatted(status.value(), errorCode, message);
            bytes = fallback.getBytes(StandardCharsets.UTF_8);
        }

        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    static class Config {

    }
}
