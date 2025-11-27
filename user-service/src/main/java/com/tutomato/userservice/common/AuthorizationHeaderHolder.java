package com.tutomato.userservice.common;

public class AuthorizationHeaderHolder {

    private static final ThreadLocal<String> AUTHORIZATION = new ThreadLocal<>();

    public static void set(String header) {
        AUTHORIZATION.set(header);
    }

    public static String get() {
        return AUTHORIZATION.get();
    }

    public static void clear() {
        AUTHORIZATION.remove();
    }

}
