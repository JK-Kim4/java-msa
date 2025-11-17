package com.tutomato.gatewayserver.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AccessLogFilter extends AbstractGatewayFilterFactory<AccessLogFilter.Config> {

    private final Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

    public AccessLogFilter() {
        super(Config.class); // ★ 반드시 지정해야 바인딩 오류가 안 남
    }

    @Override
    public GatewayFilter apply(final AccessLogFilter.Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            final ServerHttpRequest request = exchange.getRequest();
            final ServerHttpResponse response = exchange.getResponse();

            // Pre Filter
            logger.info("{}, {}", config.getBaseMessage(), request.getRemoteAddress());

            if (config.preLogger) {
                logger.info("{} Start: Request Id => {}", config.getBaseMessage(), request.getId());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        if (config.postLogger) {
                            // Post Filter
                            logger.info("Post Filter: Response Status Code is {}",
                                response.getStatusCode());
                        }
                    }
                )
            );

        }, Ordered.HIGHEST_PRECEDENCE);
    }

    public static class Config {

        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

        public String getBaseMessage() {
            return baseMessage;
        }

        public boolean isPreLogger() {
            return preLogger;
        }

        public boolean isPostLogger() {
            return postLogger;
        }

        public void setBaseMessage(String baseMessage) {
            this.baseMessage = baseMessage;
        }

        public void setPreLogger(boolean preLogger) {
            this.preLogger = preLogger;
        }

        public void setPostLogger(boolean postLogger) {
            this.postLogger = postLogger;
        }
    }
}
