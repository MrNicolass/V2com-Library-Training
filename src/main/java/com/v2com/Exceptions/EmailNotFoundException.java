package com.v2com.Exceptions;

public class EmailNotFoundException extends Exception {
    
    public EmailNotFoundException() {
        super("E-mail not found! Check the typed e-mail or ask for support!"); 
    }

    public EmailNotFoundException(String message) {
        super(message); 
    }

}
