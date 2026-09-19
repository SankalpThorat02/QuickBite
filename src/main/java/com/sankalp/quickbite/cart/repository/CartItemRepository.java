package com.sankalp.quickbite.cart.repository;

import com.sankalp.quickbite.cart.entity.Cart;
import com.sankalp.quickbite.cart.entity.CartItem;
import com.sankalp.quickbite.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndMenuItem(Cart cart, MenuItem menuItem);

    CartItem findByCart(Cart cart);
    Optional<CartItem> findByCartAndMenuItem_MenuItemId(Cart cart, Long menuItemId);
}
