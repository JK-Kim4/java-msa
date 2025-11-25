package com.tutomato.userservice.domain;

import com.tutomato.userservice.domain.dto.UserCommand;
import com.tutomato.userservice.domain.dto.UserResult;
import com.tutomato.userservice.domain.dto.UserResult.OrderResult;
import com.tutomato.userservice.domain.dto.UserResult.UserDetail;
import com.tutomato.userservice.infrastructure.UserEntity;
import com.tutomato.userservice.infrastructure.UserJpaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserJpaRepository userJpaRepository;

    public UserService(UserJpaRepository userJpaRepository) {
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

}
