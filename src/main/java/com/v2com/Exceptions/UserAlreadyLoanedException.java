package com.v2com.Exceptions;

public class UserAlreadyLoanedException extends Exception {
    
    public UserAlreadyLoanedException() {
        super("You've already borrowed this book!"); 
    }

    public UserAlreadyLoanedException(String loanId) {
        super(String.format("You've already borrowed this book, your loan code is: %s", loanId)); 
    }

}