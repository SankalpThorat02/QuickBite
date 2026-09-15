package com.sankalp.quickbite.user.exception;

public class IncorrectPasswordProvidedException extends RuntimeException {
    public IncorrectPasswordProvidedException(String message) {
        super(message);
    }
}
