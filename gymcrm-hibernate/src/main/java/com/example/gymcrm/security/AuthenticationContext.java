package com.example.gymcrm.security;

public class AuthenticationContext {
    private static final ThreadLocal<String> username = new ThreadLocal<>();
    private static final ThreadLocal<String> password = new ThreadLocal<>();

    private AuthenticationContext() {
    }

    public static String getUsername() {
        return username.get();
    }

    public static void setUsername(String user) {
        username.set(user);
    }

    public static String getPassword() {
        return password.get();
    }

    public static void setPassword(String pass) {
        password.set(pass);
    }

    public static void clear() {
        username.remove();
        password.remove();
    }
}
