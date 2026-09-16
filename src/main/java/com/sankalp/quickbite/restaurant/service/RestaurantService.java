package com.sankalp.quickbite.restaurant.service;

import com.sankalp.quickbite.menu.repository.MenuItemRepository;
import com.sankalp.quickbite.restaurant.dto.CreateRestaurantRequest;
import com.sankalp.quickbite.restaurant.dto.RestaurantResponse;
import com.sankalp.quickbite.restaurant.entity.Restaurant;
import com.sankalp.quickbite.restaurant.entity.RestaurantStatus;
import com.sankalp.quickbite.restaurant.repository.RestaurantRepository;

import com.sankalp.quickbite.user.entity.User;
import com.sankalp.quickbite.user.exception.UserNotFoundException;
import com.sankalp.quickbite.user.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Restaurant restaurant = Restaurant.builder()
                .owner(user)
                .name(request.getName())
                .address(request.getAddress())
                .status(RestaurantStatus.OPEN)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return new RestaurantResponse(
                savedRestaurant.getRestaurantId(),
                savedRestaurant.getName(),
                savedRestaurant.getAddress() ,
                savedRestaurant.getStatus()
        );
    }
}
