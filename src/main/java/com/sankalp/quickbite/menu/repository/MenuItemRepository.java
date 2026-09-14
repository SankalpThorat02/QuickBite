package com.sankalp.quickbite.menu.repository;

import com.sankalp.quickbite.menu.entity.MenuItem;
import com.sankalp.quickbite.menu.entity.MenuItemAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurantIdAndAvailability(Long restaurantId, MenuItemAvailability availability);
}
