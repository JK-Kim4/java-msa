package com.tutomato.userservice.config;

import com.tutomato.userservice.common.AuthorizationHeaderHolder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();

        rt.getInterceptors().add((request, body, execution) -> {
            String header = AuthorizationHeaderHolder.get();
            if (header != null) {
                request.getHeaders().add(HttpHeaders.AUTHORIZATION, header); // 그대로 사용
            }
            return execution.execute(request, body);
        });

        return rt;
    }
}
