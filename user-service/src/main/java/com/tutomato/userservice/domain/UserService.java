package com.tutomato.userservice.domain;

import com.tutomato.userservice.domain.authentication.TokenProvider;
import com.tutomato.userservice.domain.authentication.Tokens;
import com.tutomato.userservice.domain.dto.OrderResponse;
import com.tutomato.userservice.domain.dto.UserCommand;
import com.tutomato.userservice.domain.dto.UserResult;
import com.tutomato.userservice.domain.dto.UserResult.UserDetail;
import com.tutomato.userservice.infrastructure.UserEntity;
import com.tutomato.userservice.infrastructure.UserJpaRepository;
import com.tutomato.userservice.infrastructure.order.OrderServiceClient;
import java.time.Instant;
import java.util.List;
import org.apache.hc.core5.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Environment environment;
    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;
    private final UserJpaRepository userJpaRepository;
    private final OrderServiceClient orderServiceClient;

    private String ORDER_SERVICE_URL;

    public UserService(
        Environment environment,
        RestTemplate restTemplate,
        TokenProvider tokenProvider,
        UserJpaRepository userJpaRepository,
        OrderServiceClient orderServiceClient
    ) {
        this.environment = environment;
        this.restTemplate = restTemplate;
        this.tokenProvider = tokenProvider;
        this.userJpaRepository = userJpaRepository;
        this.orderServiceClient = orderServiceClient;
        this.ORDER_SERVICE_URL = environment.getProperty("url.order-service");
    }

    public UserResult.Create create(UserCommand.Create command) {

        logger.info("Creating user START {}", command.getName());

        User user = User.create(
            command.getEmail(),
            command.getPwd(),
            command.getName()
        );

        UserEntity entity = UserEntity.from(user);
        userJpaRepository.save(entity);

        logger.info("Creating user END {}", command.getName());

        return UserResult.Create.from(entity);
    }

    public UserResult.UserDetail getUserByUserId(String userId) {
        UserDetail detail = UserDetail.from(userJpaRepository.findByUserId(userId));

        //TODO 외부 조회
//        String orderServiceUrl = String.format(
//            ORDER_SERVICE_URL + "/%s/orders",
//            userId);
//        ResponseEntity<List<OrderResponse>> response
//            = restTemplate.exchange(
//            orderServiceUrl,
//            HttpMethod.GET,
//            null,
//            new ParameterizedTypeReference<>() {
//            }
//        );

        List<OrderResponse> orders = orderServiceClient.getOrders(userId);
        detail.setOrders(orders);

        return detail;
    }

    public UserResult.Authentication login(UserCommand.Authentication command) {
        UserEntity entity = userJpaRepository.findByEmail(command.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!entity.getPassword().equals(command.getPwd())) {
            throw new IllegalArgumentException("Invalid email or password");
        } else {
            Tokens tokens = tokenProvider.generateTokenPair(entity.getUserId(), Instant.now());
            return new UserResult.Authentication(tokens);
        }
    }

}
