package com.tutomato.userservice.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserId(String userId);

    Optional<UserEntity> findByEmail(String email);
}
