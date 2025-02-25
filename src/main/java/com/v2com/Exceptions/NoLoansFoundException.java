package com.v2com.Exceptions;

import java.util.UUID;

public class NoLoansFoundException extends Exception {

    public NoLoansFoundException(UUID loanId) {
        super(String.format("There are no loans, so, we've registered one for you! Take your book whenever you want! Loan ID: %s", loanId));
    }

}
