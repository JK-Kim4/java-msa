package com.tutomato.userservice.domain;

import com.tutomato.userservice.domain.authentication.TokenProvider;
import com.tutomato.userservice.domain.authentication.Tokens;
import com.tutomato.userservice.domain.dto.UserCommand;
import com.tutomato.userservice.domain.dto.UserResult;
import com.tutomato.userservice.domain.dto.UserResult.OrderResult;
import com.tutomato.userservice.domain.dto.UserResult.UserDetail;
import com.tutomato.userservice.infrastructure.UserEntity;
import com.tutomato.userservice.infrastructure.UserJpaRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final TokenProvider tokenProvider;
    private final UserJpaRepository userJpaRepository;

    public UserService(TokenProvider tokenProvider, UserJpaRepository userJpaRepository) {
        this.tokenProvider = tokenProvider;
        this.userJpaRepository = userJpaRepository;
    }

    public UserResult.Create create(UserCommand.Create command) {
        User user = User.create(
            command.getEmail(),
            command.getPwd(),
            command.getName()
        );

        UserEntity entity = UserEntity.from(user);
        userJpaRepository.save(entity);

        return UserResult.Create.from(entity);
    }

    public UserResult.UserDetail getUserByUserId(String userId) {
        UserDetail detail = UserDetail.from(userJpaRepository.findByUserId(userId));

        //TODO 외부 조회
        List<OrderResult> orders = List.of();
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
