package com.v2com.exceptions;

import java.util.UUID;

public class OtherUserLoanedException extends Exception {
    
    public OtherUserLoanedException() {
        super("Other person already borrowed this book!"); 
    }

    public OtherUserLoanedException(UUID reservationId) {
        super(String.format("This books is already borrowed... so, we've registered for you to borrow later! Reservation ID: %s", reservationId)); 
    }

}