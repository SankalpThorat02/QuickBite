package com.sankalp.quickbite.menu.service;

import com.sankalp.quickbite.common.security.CurrentUserService;
import com.sankalp.quickbite.menu.dto.CreateMenuItemRequest;
import com.sankalp.quickbite.menu.dto.MenuItemResponse;
import com.sankalp.quickbite.menu.dto.UpdateMenuItemAvailabilityRequest;
import com.sankalp.quickbite.menu.dto.UpdateMenuItemRequest;
import com.sankalp.quickbite.menu.entity.MenuItem;
import com.sankalp.quickbite.menu.entity.MenuItemAvailability;
import com.sankalp.quickbite.menu.exception.MenuItemNotFoundException;
import com.sankalp.quickbite.menu.mapper.MenuItemMapper;
import com.sankalp.quickbite.menu.repository.MenuItemRepository;
import com.sankalp.quickbite.restaurant.entity.Restaurant;
import com.sankalp.quickbite.restaurant.entity.RestaurantStatus;
import com.sankalp.quickbite.restaurant.exception.RestaurantNotFoundException;
import com.sankalp.quickbite.restaurant.exception.UnauthorizedRestaurantAccessException;
import com.sankalp.quickbite.restaurant.repository.RestaurantRepository;
import com.sankalp.quickbite.user.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;
    private final CurrentUserService currentUserService;

    public MenuService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository, MenuItemMapper menuItemMapper, CurrentUserService currentUserService) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository =  restaurantRepository;
        this.menuItemMapper = menuItemMapper;
        this.currentUserService = currentUserService;
    }

    public List<MenuItemResponse> getMenuItems(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findByRestaurantIdAndStatusNot(restaurantId, RestaurantStatus.SUSPENDED)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));

        List<MenuItem> menuItems = menuItemRepository.findByRestaurantAndAvailability(restaurant, MenuItemAvailability.AVAILABLE);

        return menuItems
                .stream()
                .map(menuItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MenuItemResponse getMenuItem(Long restaurantId, Long menuItemId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));

        MenuItem menuItem = menuItemRepository.findByMenuItemIdAndRestaurantAndAvailability(menuItemId, restaurant, MenuItemAvailability.AVAILABLE)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item with ID: " + menuItemId + " not found"));

        return menuItemMapper.toResponse(menuItem);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public List<MenuItemResponse> getAllMenuItems(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new  RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));

        User user = currentUserService.getCurrentUser();

        if(!restaurant.getOwner().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedRestaurantAccessException("You are not the owner of this restaurant");
        }

        List<MenuItem> menuItems = menuItemRepository.findByRestaurant(restaurant);

        return menuItems
                .stream()
                .map(menuItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public MenuItemResponse addMenuItem(Long restaurantId, CreateMenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));

        User user = currentUserService.getCurrentUser();

        if (!restaurant.getOwner().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedRestaurantAccessException("Cannot access this restaurant");
        }

        MenuItem menuItem = MenuItem.builder()
                .restaurant(restaurant)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .availability(MenuItemAvailability.AVAILABLE)
                .build();

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return menuItemMapper.toResponse(savedMenuItem);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public MenuItemResponse updateMenuItem(Long restaurantId, Long menuItemId, UpdateMenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));

        User user = currentUserService.getCurrentUser();

        if(!restaurant.getOwner().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedRestaurantAccessException("Cannot access this restaurant");
        }

        MenuItem menuItem = menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, restaurant)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item with ID: " + menuItemId + " not found"));

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return menuItemMapper.toResponse(savedMenuItem);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public MenuItemResponse updateMenuItemAvailability(Long restaurantId, Long menuItemId, UpdateMenuItemAvailabilityRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));

        User user = currentUserService.getCurrentUser();

        if(!restaurant.getOwner().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedRestaurantAccessException("Cannot access this restaurant");
        }

        MenuItem menuItem = menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, restaurant)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item with ID: " + menuItemId + " not found"));

        menuItem.setAvailability(request.getMenuItemAvailability());
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return menuItemMapper.toResponse(savedMenuItem);
    }
}
