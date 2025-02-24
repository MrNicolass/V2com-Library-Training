package com.v2com.Exceptions;

public class LoanNotFoundException extends Exception {
    
    public LoanNotFoundException() {
        super("Loan(s) not found!"); 
    }

    public LoanNotFoundException(String loanId) {
        super(String.format("Loan %s not found!", loanId)); 
    }
    
}