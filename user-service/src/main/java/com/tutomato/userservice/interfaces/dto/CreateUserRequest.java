package com.tutomato.userservice.interfaces.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

    @NotNull(message = "이메일을 입력해주세요")
    @Size(min = 2, message = "Email must be at least 2 characters long")
    @Email
    String email,

    @NotNull(message = "패스워드를 입력해줘세요")
    @Size(min = 12, message = "Password must be at least 12 characters long")
    String pwd,

    @NotNull(message = "이름을 입력해주세요")
    @Size(min = 2, message = "Name must be at least 2 characters long")
    String name
) {

}
