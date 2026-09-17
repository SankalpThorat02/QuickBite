package com.sankalp.quickbite.restaurant.exception;

public class ForbiddenStatusUpdateException extends RuntimeException {
    public ForbiddenStatusUpdateException(String message) {
        super(message);
    }
}
