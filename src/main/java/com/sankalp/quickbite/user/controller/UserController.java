package com.sankalp.quickbite.user.controller;

import com.sankalp.quickbite.user.dto.UserRequest;
import com.sankalp.quickbite.user.dto.UserResponse;
import com.sankalp.quickbite.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

//    @PostMapping
//    public ResponseEntity<UserResponse> addUser(@RequestBody @Valid UserRequest userRequest) {
//
//    }
}
