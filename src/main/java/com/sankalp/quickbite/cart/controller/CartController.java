package com.sankalp.quickbite.cart.controller;

import com.sankalp.quickbite.cart.dto.AddCartItemRequest;
import com.sankalp.quickbite.cart.dto.CartResponse;
import com.sankalp.quickbite.cart.dto.SetCartItemQuantityRequest;
import com.sankalp.quickbite.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartResponse getCart() {
        return cartService.getCart();
    }

    @PostMapping
    public CartResponse addItem(@RequestBody @Valid AddCartItemRequest request) {
        return cartService.addItem(request);
    }

    @DeleteMapping("/items/{menuItemId}")
    public CartResponse removeItem(@PathVariable Long menuItemId) {
        return cartService.removeItem(menuItemId);
    }

    @PatchMapping("/items/{menuItemId}")
    public CartResponse setItemQuantity(@PathVariable Long menuItemId, @RequestBody @Valid SetCartItemQuantityRequest request) {
        return cartService.setItemQuantity(menuItemId, request);
    }

    @DeleteMapping
    public CartResponse deleteCart() {
        return cartService.deleteCart();
    }
}
