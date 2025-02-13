package com.v2com.Exceptions;

public class FilterInvalidException extends Exception {

    public FilterInvalidException() {
        super("One or more filters are invalid!"); 
    }

    public FilterInvalidException(String filter) {
        super(String.format("Filter(s) not found: %s", filter)); 
    }

}