package com.tutomato.userservice.domain;

public class User {

    String userId;
    String email;
    String password;
    String name;

    protected User(String userId, String email, String password, String name) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User create(String email, String password, String name) {
        String userId = java.util.UUID.randomUUID().toString();

        return new User(userId, email, password, name);
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
