package com.v2com.Exceptions;

import java.util.UUID;

public class OtherUserLoaned extends Exception {
    
    public OtherUserLoaned() {
        super("Other person already borrowed this book!"); 
    }

    public OtherUserLoaned(UUID reservationId) {
        super(String.format("This books is already borrowed... so, we've registered for you to borrow later! Reservation ID: %s", reservationId)); 
    }

}