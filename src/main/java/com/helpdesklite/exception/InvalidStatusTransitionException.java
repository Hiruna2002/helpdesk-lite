package com.helpdesklite.exception;
public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String m){
        super(m);
    }
}
