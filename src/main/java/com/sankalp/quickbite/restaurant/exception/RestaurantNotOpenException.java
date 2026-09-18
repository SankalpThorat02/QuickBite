package com.sankalp.quickbite.restaurant.exception;

public class RestaurantNotOpenException extends RuntimeException {
    public RestaurantNotOpenException(String message) {
        super(message);
    }
}
