package com.sankalp.quickbite.common.exception;

import com.sankalp.quickbite.auth.exception.AdminAccountCreationNotAllowedException;
import com.sankalp.quickbite.cart.exception.CartItemRestaurantMismatchException;
import com.sankalp.quickbite.menu.exception.MenuItemNotAvailableException;
import com.sankalp.quickbite.menu.exception.MenuItemNotFoundException;
import com.sankalp.quickbite.menu.exception.MenuItemRestaurantMismatchException;
import com.sankalp.quickbite.restaurant.exception.ForbiddenStatusUpdateException;
import com.sankalp.quickbite.restaurant.exception.RestaurantNotFoundException;
import com.sankalp.quickbite.restaurant.exception.RestaurantNotOpenException;
import com.sankalp.quickbite.restaurant.exception.UnauthorizedRestaurantAccessException;
import com.sankalp.quickbite.user.exception.IncorrectPasswordProvidedException;
import com.sankalp.quickbite.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<String> handleRestaurantNotFoundException(RestaurantNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(AdminAccountCreationNotAllowedException.class)
    public ResponseEntity<String> handleAdminAccountCreationNotAllowedException(AdminAccountCreationNotAllowedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordProvidedException.class)
    public ResponseEntity<String> handleIncorrectPasswordProvidedException(IncorrectPasswordProvidedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedRestaurantAccessException.class)
    public ResponseEntity<String> handleUnauthorizedRestaurantAccessException(UnauthorizedRestaurantAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenStatusUpdateException.class)
    public ResponseEntity<String> handleForbiddenStatusUpdateException(ForbiddenStatusUpdateException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MenuItemNotFoundException.class)
    public ResponseEntity<String> handleMenuItemNotFoundException(MenuItemNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MenuItemRestaurantMismatchException.class)
    public ResponseEntity<String> handleMenuItemRestaurantMismatchException(MenuItemRestaurantMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MenuItemNotAvailableException.class)
    public ResponseEntity<String> handleMenuItemNotAvailableException(MenuItemNotAvailableException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(RestaurantNotOpenException.class)
    public ResponseEntity<String> handleRestaurantNotOpenException(RestaurantNotOpenException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(CartItemRestaurantMismatchException.class)
    public ResponseEntity<String> handleCartItemRestaurantMismatchException(CartItemRestaurantMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
