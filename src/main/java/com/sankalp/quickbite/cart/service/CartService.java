package com.sankalp.quickbite.cart.service;

import com.sankalp.quickbite.cart.dto.AddCartItemRequest;
import com.sankalp.quickbite.cart.dto.CartItemResponse;
import com.sankalp.quickbite.cart.dto.CartResponse;
import com.sankalp.quickbite.cart.dto.SetCartItemQuantityRequest;
import com.sankalp.quickbite.cart.entity.Cart;
import com.sankalp.quickbite.cart.entity.CartItem;
import com.sankalp.quickbite.cart.exception.CartEmptyException;
import com.sankalp.quickbite.cart.exception.CartItemNotFoundException;
import com.sankalp.quickbite.cart.exception.CartItemRestaurantMismatchException;
import com.sankalp.quickbite.cart.repository.CartItemRepository;
import com.sankalp.quickbite.cart.repository.CartRepository;
import com.sankalp.quickbite.common.security.CurrentUserService;
import com.sankalp.quickbite.menu.entity.MenuItem;
import com.sankalp.quickbite.menu.entity.MenuItemAvailability;
import com.sankalp.quickbite.menu.exception.MenuItemNotAvailableException;
import com.sankalp.quickbite.menu.exception.MenuItemNotFoundException;
import com.sankalp.quickbite.menu.repository.MenuItemRepository;
import com.sankalp.quickbite.restaurant.entity.Restaurant;
import com.sankalp.quickbite.restaurant.entity.RestaurantStatus;
import com.sankalp.quickbite.restaurant.exception.RestaurantNotOpenException;
import com.sankalp.quickbite.user.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CurrentUserService currentUserService;
    private final MenuItemRepository menuItemRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CurrentUserService currentUserService, MenuItemRepository menuItemRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.currentUserService = currentUserService;
        this.menuItemRepository = menuItemRepository;
        this.cartItemRepository = cartItemRepository;
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

    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public CartResponse addItem(AddCartItemRequest request) {

        User user = currentUserService.getCurrentUser();

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item with ID: " +  request.getMenuItemId() + " not found"));

        if(menuItem.getAvailability() != MenuItemAvailability.AVAILABLE){
            throw new MenuItemNotAvailableException("Menu Item with ID : " + menuItem.getMenuItemId() + " is not available");
        }

        Restaurant restaurant = menuItem.getRestaurant();
        if(restaurant.getStatus() != RestaurantStatus.OPEN){
            throw new RestaurantNotOpenException("Restaurant " + restaurant.getName() + " not open");
        }

        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart;
        if(optionalCart.isEmpty()){
            cart = Cart.builder()
                    .user(user)
                    .items(new ArrayList<>())
                    .build();

            cart = cartRepository.save(cart);
        }
        else {
            cart = optionalCart.get();
        }

        List<CartItem> items = cart.getItems();
        if(!items.isEmpty()){
            Long currentRestaurantId = items.getFirst().getMenuItem().getRestaurant().getRestaurantId();
            if(!menuItem.getRestaurant().getRestaurantId().equals(currentRestaurantId)){
                throw new CartItemRestaurantMismatchException("Cart contains items from another restaurant");
            }
        }

        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndMenuItem(cart, menuItem);
        if(optionalCartItem.isEmpty()){
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(request.getQuantity())
                    .build();

            cart.addItem(cartItem);
            cartRepository.save(cart);
        }
        else {
            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());

            cartItemRepository.save(cartItem);
        }
//
//        List<CartItemResponse> cartItemResponses = items
//                .stream()
//                .map(item -> {
//                    MenuItem currentMenuItem = item.getMenuItem();
//
//                    Long menuItemId = currentMenuItem.getMenuItemId();
//                    String name = currentMenuItem.getName();
//                    BigDecimal unitPrice = currentMenuItem.getPrice();
//
//                    BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
//
//                    return new CartItemResponse(
//                            menuItemId,
//                            name,
//                            item.getQuantity(),
//                            unitPrice,
//                            lineTotal
//                    );
//                })
//                .collect(Collectors.toList());
//
//        BigDecimal total = cartItemResponses
//                .stream()
//                .map(CartItemResponse::getLineTotal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        return new CartResponse(
//                cartItemResponses,
//                total
//        );

        return getCartResponse(cart);
    }

    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public CartResponse removeItem(Long menuItemId) {
        User user = currentUserService.getCurrentUser();

        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        if(optionalCart.isEmpty()){
            throw new CartEmptyException("Cart is empty");
        }

        Cart cart = optionalCart.get();
        CartItem item = cartItemRepository.findByCartAndMenuItem_MenuItemId(cart, menuItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        cart.removeItem(item);

//        List<CartItem> items = cart.getItems();
//
//        List<CartItemResponse> cartItemResponses = items
//                .stream()
//                .map(cartItem -> {
//                    MenuItem currentMenuItem = cartItem.getMenuItem();
//
//                    Long currentMenuItemId = currentMenuItem.getMenuItemId();
//                    String name = currentMenuItem.getName();
//                    BigDecimal unitPrice = currentMenuItem.getPrice();
//
//                    BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
//
//                    return new CartItemResponse(
//                            currentMenuItemId,
//                            name,
//                            cartItem.getQuantity(),
//                            unitPrice,
//                            lineTotal
//                    );
//                })
//                .collect(Collectors.toList());
//
//        BigDecimal total = cartItemResponses
//                .stream()
//                .map(CartItemResponse::getLineTotal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        return new CartResponse(
//                cartItemResponses,
//                total
//        );

        return getCartResponse(cart);
    }

    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public CartResponse setItemQuantity(Long menuItemId, SetCartItemQuantityRequest request) {
        User user = currentUserService.getCurrentUser();

        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        if(optionalCart.isEmpty()){
            throw new CartEmptyException("Cart is empty");
        }

        Cart cart = optionalCart.get();
        CartItem item = cartItemRepository.findByCartAndMenuItem_MenuItemId(cart, menuItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        item.setQuantity(request.getQuantity());

        List<CartItem> items = cart.getItems();

        List<CartItemResponse> cartItemResponses = items
                .stream()
                .map(cartItem -> {
                    MenuItem currentMenuItem = cartItem.getMenuItem();

                    Long currentMenuItemId = currentMenuItem.getMenuItemId();
                    String name = currentMenuItem.getName();
                    BigDecimal unitPrice = currentMenuItem.getPrice();

                    BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                    return new CartItemResponse(
                            currentMenuItemId,
                            name,
                            cartItem.getQuantity(),
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

        return getCartResponse(cart);
    }

    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public CartResponse deleteCart() {
        User user = currentUserService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartEmptyException("Cart is empty"));

        cart.clearItems();

        return new CartResponse(
                new ArrayList<>(),
                BigDecimal.ZERO
        );
    }

    private CartResponse getCartResponse(Cart cart) {

        List<CartItem> items = cart.getItems();

        List<CartItemResponse> cartItemResponses = items
                .stream()
                .map(cartItem -> {
                    MenuItem currentMenuItem = cartItem.getMenuItem();

                    Long currentMenuItemId = currentMenuItem.getMenuItemId();
                    String name = currentMenuItem.getName();
                    BigDecimal unitPrice = currentMenuItem.getPrice();

                    BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                    return new CartItemResponse(
                            currentMenuItemId,
                            name,
                            cartItem.getQuantity(),
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
