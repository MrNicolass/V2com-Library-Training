package com.v2com.exceptions;

public class PasswordIncorrectException extends Exception {

    public PasswordIncorrectException() {
        super("Password is incorrect!");
    }

}