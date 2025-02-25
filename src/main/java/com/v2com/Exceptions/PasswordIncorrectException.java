package com.v2com.Exceptions;

public class PasswordIncorrectException extends Exception {

    public PasswordIncorrectException() {
        super("Password is incorrect!");
    }

}