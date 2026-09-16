package com.sankalp.quickbite.restaurant.repository;

import com.sankalp.quickbite.restaurant.entity.Restaurant;
import com.sankalp.quickbite.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByOwner(User owner);
}
