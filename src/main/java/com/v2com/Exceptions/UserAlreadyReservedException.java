package com.v2com.Exceptions;

public class UserAlreadyReservedException extends Exception {

    public UserAlreadyReservedException() {
        super("You already made a reserve for this book!");
    }
    
}