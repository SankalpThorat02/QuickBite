package com.sankalp.quickbite.restaurant.exception;

public class UnauthorizedRestaurantAccessException extends RuntimeException {
    public UnauthorizedRestaurantAccessException(String message) {
        super(message);
    }
}
