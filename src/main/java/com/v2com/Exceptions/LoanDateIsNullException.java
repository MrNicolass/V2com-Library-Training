package com.v2com.Exceptions;

public class LoanDateIsNullException extends Exception{

    public LoanDateIsNullException() {
        super("When was the book loaned? Fill the date!"); 
    }

}