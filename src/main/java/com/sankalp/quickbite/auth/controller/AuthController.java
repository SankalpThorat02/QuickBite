package com.sankalp.quickbite.auth.controller;

import com.sankalp.quickbite.auth.dto.AuthResponse;
import com.sankalp.quickbite.auth.dto.LoginRequest;
import com.sankalp.quickbite.auth.dto.SignupRequest;
import com.sankalp.quickbite.auth.service.AuthService;
import com.sankalp.quickbite.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid SignupRequest request) {
        UserResponse user = authService.signUp(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }
}
