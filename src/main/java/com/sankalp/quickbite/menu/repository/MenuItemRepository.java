package com.sankalp.quickbite.menu.repository;

import com.sankalp.quickbite.menu.entity.MenuItem;
import com.sankalp.quickbite.menu.entity.MenuItemAvailability;
import com.sankalp.quickbite.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurantAndAvailability(Restaurant restaurant, MenuItemAvailability availability);
    Optional<MenuItem> findByMenuItemIdAndRestaurant(Long menuItemId, Restaurant restaurant);
}
