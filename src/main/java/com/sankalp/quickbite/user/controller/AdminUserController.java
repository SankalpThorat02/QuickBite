package com.sankalp.quickbite.user.controller;

import com.sankalp.quickbite.user.dto.StatusUpdateRequest;
import com.sankalp.quickbite.user.dto.UserResponse;
import com.sankalp.quickbite.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/users/{userId}/status")
    public UserResponse updateUserStatus(@PathVariable Long userId, @RequestBody StatusUpdateRequest request) {
        return userService.updateUserStatus(userId, request);
    }
}
