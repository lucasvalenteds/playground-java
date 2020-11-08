package com.playground.java.pitest.sample;

public class PasswordValidator {

    public boolean validate(String password) {
        return password.length() >= 8;
    }
}
