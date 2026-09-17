package com.sankalp.quickbite.restaurant.service;

import com.sankalp.quickbite.common.security.CurrentUserService;
import com.sankalp.quickbite.menu.dto.MenuItemResponse;
import com.sankalp.quickbite.menu.entity.MenuItem;
import com.sankalp.quickbite.menu.entity.MenuItemAvailability;
import com.sankalp.quickbite.menu.repository.MenuItemRepository;
import com.sankalp.quickbite.restaurant.dto.CreateRestaurantRequest;
import com.sankalp.quickbite.restaurant.dto.RestaurantResponse;
import com.sankalp.quickbite.restaurant.dto.RestaurantInfoUpdateRequest;
import com.sankalp.quickbite.restaurant.dto.RestaurantStatusUpdateRequest;
import com.sankalp.quickbite.restaurant.entity.Restaurant;
import com.sankalp.quickbite.restaurant.entity.RestaurantStatus;
import com.sankalp.quickbite.restaurant.exception.ForbiddenStatusUpdateException;
import com.sankalp.quickbite.restaurant.exception.RestaurantNotFoundException;
import com.sankalp.quickbite.restaurant.exception.UnauthorizedRestaurantAccessException;
import com.sankalp.quickbite.restaurant.mapper.RestaurantMapper;
import com.sankalp.quickbite.restaurant.repository.RestaurantRepository;

import com.sankalp.quickbite.user.entity.User;
import com.sankalp.quickbite.user.exception.UserNotFoundException;
import com.sankalp.quickbite.user.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemRepository menuItemRepository;
    private final CurrentUserService currentUserService;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, RestaurantMapper restaurantMapper, MenuItemRepository menuItemRepository, CurrentUserService currentUserService) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.restaurantMapper = restaurantMapper;
        this.menuItemRepository = menuItemRepository;
        this.currentUserService = currentUserService;
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        User user = currentUserService.getCurrentUser();

        Restaurant restaurant = Restaurant.builder()
                .owner(user)
                .name(request.getName())
                .address(request.getAddress())
                .status(RestaurantStatus.OPEN)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(savedRestaurant);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public List<RestaurantResponse> getMyRestaurants() {
        User user = currentUserService.getCurrentUser();

        List<Restaurant> restaurants = restaurantRepository.findByOwner(user);

        return restaurants
                .stream()
                .map(restaurant -> restaurantMapper.toResponse(restaurant))
                .collect(Collectors.toList());
    }

    public List<RestaurantResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants
                .stream()
                .map(restaurant -> restaurantMapper.toResponse(restaurant))
                .collect(Collectors.toList());
    }

    public RestaurantResponse getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID: " +  restaurantId + " not found"));

        return restaurantMapper.toResponse(restaurant);
    }

    public List<MenuItemResponse> getMenuItems(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));

        List<MenuItem> menuItems = menuItemRepository.findByRestaurantAndAvailability(restaurant, MenuItemAvailability.AVAILABLE);

        return menuItems
                .stream()
                .map(menuItem -> new MenuItemResponse(
                    menuItem.getMenuItemId(),
                    menuItem.getName(),
                    menuItem.getDescription(),
                    menuItem.getPrice(),
                    menuItem.getAvailability()
                )).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public RestaurantResponse updateRestaurantInfo(Long restaurantId, RestaurantInfoUpdateRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));

        User user = currentUserService.getCurrentUser();

        if (!restaurant.getOwner().getUserId().equals(user.getUserId())){
            throw new UnauthorizedRestaurantAccessException("Cannot access this restaurant");
        }

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(savedRestaurant);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public RestaurantResponse updateRestaurantStatus(Long restaurantId, RestaurantStatusUpdateRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new  RestaurantNotFoundException("Restaurant with ID: " + restaurantId + " not found"));

        User user = currentUserService.getCurrentUser();

        if(!restaurant.getOwner().getUserId().equals(user.getUserId())){
            throw new UnauthorizedRestaurantAccessException("Cannot access this restaurant");
        }

        if(request.getStatus() == RestaurantStatus.OPEN || request.getStatus() == RestaurantStatus.CLOSED){
            restaurant.setStatus(request.getStatus());
        }
        else {
            throw new ForbiddenStatusUpdateException("You dont have permissions to update to this status");
        }

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(savedRestaurant);
    }
}
