package com.sankalp.quickbite.restaurant.controller;

import com.sankalp.quickbite.menu.dto.MenuItemResponse;
import com.sankalp.quickbite.restaurant.dto.RestaurantResponse;
import com.sankalp.quickbite.restaurant.service.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{restaurantId}/menu")
    public List<MenuItemResponse> getRestaurantMenu(@PathVariable Long restaurantId) {
        return restaurantService.getAllMenuItems(restaurantId);
    }
}
