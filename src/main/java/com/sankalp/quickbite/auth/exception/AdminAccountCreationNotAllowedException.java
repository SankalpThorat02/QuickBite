package com.sankalp.quickbite.auth.exception;

public class AdminAccountCreationNotAllowedException extends RuntimeException {
    public AdminAccountCreationNotAllowedException(String message) {
        super(message);
    }
}
