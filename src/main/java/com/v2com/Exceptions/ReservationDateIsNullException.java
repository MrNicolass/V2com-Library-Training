package com.v2com.Exceptions;

public class ReservationDateIsNullException extends Exception {

    public ReservationDateIsNullException() {
        super("When was the book reserved? Fill the date!");
    }

}