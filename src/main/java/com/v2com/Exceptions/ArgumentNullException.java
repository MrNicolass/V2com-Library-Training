package com.v2com.Exceptions;

public class ArgumentNullException extends Exception {

    public ArgumentNullException(String argument) {
        super(String.format("%s is required!", argument));
    }

}