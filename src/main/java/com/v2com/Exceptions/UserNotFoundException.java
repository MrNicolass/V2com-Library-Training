package com.v2com.Exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found!"); 
    }

    public UserNotFoundException(String userId) {
        super(String.format("User %s not found!", userId)); 
    }
    
}
