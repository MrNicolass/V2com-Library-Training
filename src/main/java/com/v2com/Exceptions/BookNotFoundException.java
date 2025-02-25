package com.v2com.Exceptions;

import java.util.UUID;

public class BookNotFoundException extends Exception{
    
    public BookNotFoundException() {
        super("Book not found!"); 
    }

    public BookNotFoundException(UUID bookId) {
        super(String.format("Book %s not found!", bookId)); 
    }

    public BookNotFoundException(String message) {
        super(message); 
    }

}