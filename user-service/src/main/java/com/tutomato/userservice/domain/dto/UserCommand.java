package com.tutomato.userservice.domain.dto;

import com.tutomato.userservice.interfaces.dto.CreateUserRequest;

public class UserCommand {

    public static class Create {

        String email;
        String pwd;
        String name;

        public Create(String email, String pwd, String name) {
            this.email = email;
            this.pwd = pwd;
            this.name = name;
        }

        public static Create from(CreateUserRequest request) {
            return new Create(
                request.email(),
                request.pwd(),
                request.name()
            );
        }

        public String getEmail() {
            return email;
        }

        public String getPwd() {
            return pwd;
        }

        public String getName() {
            return name;
        }


    }


    public static class Authentication {

        String email;
        String pwd;

        protected Authentication(String email, String pwd) {
            this.email = email;
            this.pwd = pwd;
        }

        public static Authentication of(String email, String pwd) {
            return new Authentication(email, pwd);
        }

        public String getEmail() {
            return email;
        }

        public String getPwd() {
            return pwd;
        }

    }
}
