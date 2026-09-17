package com.sankalp.quickbite.restaurant.controller;

import com.sankalp.quickbite.menu.dto.CreateMenuItemRequest;
import com.sankalp.quickbite.menu.dto.MenuItemResponse;
import com.sankalp.quickbite.menu.dto.UpdateMenuItemRequest;
import com.sankalp.quickbite.menu.service.MenuService;
import com.sankalp.quickbite.restaurant.dto.CreateRestaurantRequest;
import com.sankalp.quickbite.restaurant.dto.RestaurantResponse;
import com.sankalp.quickbite.restaurant.dto.RestaurantInfoUpdateRequest;
import com.sankalp.quickbite.restaurant.dto.RestaurantStatusUpdateRequest;
import com.sankalp.quickbite.restaurant.entity.RestaurantStatus;
import com.sankalp.quickbite.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MenuService menuService;

    public RestaurantController(RestaurantService restaurantService, MenuService menuService) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody @Valid CreateRestaurantRequest request) {
        RestaurantResponse restaurant = restaurantService.createRestaurant(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurant);
    }

    @GetMapping("/my")
    public List<RestaurantResponse> getMyRestaurants() {
        return restaurantService.getMyRestaurants();
    }

    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{restaurantId}")
    public RestaurantResponse getRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.getRestaurant(restaurantId);
    }

    @GetMapping("/{restaurantId}/menu")
    public List<MenuItemResponse> getMenuItems(@PathVariable Long restaurantId) {
        return menuService.getMenuItems(restaurantId);
    }

    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<MenuItemResponse> addMenuItem(@PathVariable Long restaurantId, @RequestBody @Valid CreateMenuItemRequest request) {
        MenuItemResponse menuItem = menuService.addMenuItem(restaurantId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(menuItem);
    }

    @PutMapping("/{restaurantId}/menu/{menuItemId}")
    public MenuItemResponse updateMenuItem(@PathVariable Long restaurantId, @PathVariable Long menuItemId, @RequestBody @Valid UpdateMenuItemRequest request) {
        return menuService.updateMenuItem(restaurantId, menuItemId, request);
    }

    @PutMapping("/{restaurantId}")
    public RestaurantResponse updateRestaurantInfo(@PathVariable Long restaurantId, @RequestBody @Valid RestaurantInfoUpdateRequest request) {
        return restaurantService.updateRestaurantInfo(restaurantId, request);
    }

    @PatchMapping("/{restaurantId}/status")
    public RestaurantResponse updateRestaurantStatus(@PathVariable Long restaurantId, @RequestBody @Valid RestaurantStatusUpdateRequest request) {
        return restaurantService.updateRestaurantStatus(restaurantId, request);
    }
}
