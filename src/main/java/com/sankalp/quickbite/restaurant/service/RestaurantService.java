package com.sankalp.quickbite.restaurant.service;

import com.sankalp.quickbite.menu.dto.MenuItemResponse;
import com.sankalp.quickbite.menu.entity.MenuItem;
import com.sankalp.quickbite.menu.entity.MenuItemAvailability;
import com.sankalp.quickbite.menu.repository.MenuItemRepository;
import com.sankalp.quickbite.restaurant.dto.RestaurantResponse;
import com.sankalp.quickbite.restaurant.entity.Restaurant;
import com.sankalp.quickbite.restaurant.exception.RestaurantNotFoundException;
import com.sankalp.quickbite.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<RestaurantResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .map(restaurant -> new RestaurantResponse(
                        restaurant.getRestaurantId(),
                        restaurant.getName(),
                        restaurant.getAddress(),
                        restaurant.getStatus()
                )).collect(Collectors.toList());
    }

    public List<MenuItemResponse> getAllMenuItems(Long restaurantId) {
        boolean isRestaurantPresent = restaurantRepository.existsById(restaurantId);

        if(!isRestaurantPresent) {
            throw (new RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));
        }

        List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndAvailability(restaurantId, MenuItemAvailability.AVAILABLE);

        return menuItems.stream()
                .map(menuItem -> new MenuItemResponse(
                        menuItem.getMenuItemId(),
                        menuItem.getName(),
                        menuItem.getDescription(),
                        menuItem.getPrice(),
                        menuItem.getAvailability()
                )).collect(Collectors.toList());
    }
}
