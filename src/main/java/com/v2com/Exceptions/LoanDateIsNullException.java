package com.v2com.exceptions;

public class LoanDateIsNullException extends Exception{

    public LoanDateIsNullException() {
        super("When was the book loaned? Fill the date!"); 
    }

}