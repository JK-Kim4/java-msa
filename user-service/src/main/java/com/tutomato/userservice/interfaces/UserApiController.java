package com.tutomato.userservice.interfaces;

import com.tutomato.userservice.domain.UserService;
import com.tutomato.userservice.domain.dto.UserCommand;
import com.tutomato.userservice.domain.dto.UserResult;
import com.tutomato.userservice.domain.dto.UserResult.UserDetail;
import com.tutomato.userservice.interfaces.dto.CreateUserRequest;
import com.tutomato.userservice.interfaces.dto.CreateUserResponse;
import com.tutomato.userservice.interfaces.dto.UserResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    private final UserService userService;
    private final Environment environment;

    public UserApiController(UserService userService, Environment environment) {
        this.userService = userService;
        this.environment = environment;
    }

    @PostMapping("/users")
    public ResponseEntity<CreateUserResponse> create(
        @RequestBody CreateUserRequest request
    ) {
        UserResult.Create result = userService.create(UserCommand.Create.from(request));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CreateUserResponse.from(result));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponse> findByUserId(
        @PathVariable("userId") String userId
    ) {
        UserDetail detail = userService.getUserByUserId(userId);

        return ResponseEntity.ok(UserResponse.from(detail));
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return String.format("Catalog service is running on local port %s (server port: %s)",
            environment.getProperty("local.server.port"),
            environment.getProperty("local.server.port"));
    }

}
