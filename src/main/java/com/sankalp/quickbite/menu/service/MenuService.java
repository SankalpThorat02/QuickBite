package com.sankalp.quickbite.menu.service;

import com.sankalp.quickbite.common.security.CurrentUserService;
import com.sankalp.quickbite.menu.dto.CreateMenuItemRequest;
import com.sankalp.quickbite.menu.dto.MenuItemResponse;
import com.sankalp.quickbite.menu.entity.MenuItem;
import com.sankalp.quickbite.menu.entity.MenuItemAvailability;
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
}
