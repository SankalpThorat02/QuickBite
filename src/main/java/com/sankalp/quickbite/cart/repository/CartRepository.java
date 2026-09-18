package com.sankalp.quickbite.cart.repository;

import com.sankalp.quickbite.cart.entity.Cart;
import com.sankalp.quickbite.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUser(User user);
}
