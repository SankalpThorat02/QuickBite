package com.sankalp.quickbite.user.controller;

import com.sankalp.quickbite.user.dto.ChangePasswordRequest;
import com.sankalp.quickbite.user.dto.UpdateUserInfoRequest;
import com.sankalp.quickbite.user.dto.UserResponse;
import com.sankalp.quickbite.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse me() {
        return userService.me();
    }

    @PutMapping("/me")
    public UserResponse updateInfo(@RequestBody @Valid UpdateUserInfoRequest request) {
        return userService.updateInfo(request);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request);

        return ResponseEntity.noContent().build();
    }
}
