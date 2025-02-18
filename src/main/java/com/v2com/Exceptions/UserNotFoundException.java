package com.v2com.exceptions;

import java.util.UUID;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found!"); 
    }

    public UserNotFoundException(UUID userId) {
        super(String.format("User %s not found!", userId)); 
    }

    public UserNotFoundException(String message) {
        super(message); 
    }
    
}
