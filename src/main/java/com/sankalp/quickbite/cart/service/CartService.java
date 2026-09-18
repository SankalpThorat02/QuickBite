package com.sankalp.quickbite.cart.service;

import com.sankalp.quickbite.cart.dto.CartItemResponse;
import com.sankalp.quickbite.cart.dto.CartResponse;
import com.sankalp.quickbite.cart.entity.Cart;
import com.sankalp.quickbite.cart.entity.CartItem;
import com.sankalp.quickbite.cart.repository.CartRepository;
import com.sankalp.quickbite.common.security.CurrentUserService;
import com.sankalp.quickbite.menu.entity.MenuItem;
import com.sankalp.quickbite.user.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CurrentUserService currentUserService;

    public CartService(CartRepository cartRepository, CurrentUserService currentUserService) {
        this.cartRepository = cartRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CUSTOMER')")
    public CartResponse getCart() {
        User user = currentUserService.getCurrentUser();

        Optional<Cart> optionalCart  = cartRepository.findByUser(user);
        if(optionalCart.isEmpty()){
            return new CartResponse(
                    Collections.emptyList(),
                    BigDecimal.ZERO
            );
        }

        Cart cart = optionalCart.get();
        List<CartItem> items = cart.getItems();

        List<CartItemResponse> cartItemResponses = items
                .stream()
                .map(item -> {
                    MenuItem menuItem = item.getMenuItem();

                    Long menuItemId = menuItem.getMenuItemId();
                    String name = menuItem.getName();
                    BigDecimal unitPrice = menuItem.getPrice();

                    BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

                    return new CartItemResponse(
                            menuItemId,
                            name,
                            item.getQuantity(),
                            unitPrice,
                            lineTotal
                    );
                })
                .collect(Collectors.toList());

        BigDecimal total = cartItemResponses
                .stream()
                .map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
                cartItemResponses,
                total
        );
    }
}
