package com.v2com.Exceptions;

public class ReservationNotFoundException extends Exception {
    
    public ReservationNotFoundException() {
        super("Reservation(s) not found!"); 
    }

    public ReservationNotFoundException(String reservationId) {
        super(String.format("Reservation %s not found!", reservationId)); 
    }
    
}