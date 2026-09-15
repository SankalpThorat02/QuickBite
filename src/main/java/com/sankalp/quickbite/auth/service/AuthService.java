package com.sankalp.quickbite.auth.service;

import com.sankalp.quickbite.auth.dto.AuthResponse;
import com.sankalp.quickbite.auth.dto.LoginRequest;
import com.sankalp.quickbite.auth.dto.SignupRequest;
import com.sankalp.quickbite.auth.exception.AdminAccountCreationNotAllowedException;
import com.sankalp.quickbite.user.dto.UserResponse;
import com.sankalp.quickbite.user.entity.User;
import com.sankalp.quickbite.user.entity.UserRole;
import com.sankalp.quickbite.user.entity.UserStatus;
import com.sankalp.quickbite.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserResponse signUp(SignupRequest request) {
        if(request.getRole() == UserRole.ADMIN) {
            throw new AdminAccountCreationNotAllowedException("Account with ADMIN role cannot be created");
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordHash)
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();

        User createdUser = userRepository.save(user);

        return new UserResponse (
                createdUser.getUserId(),
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getRole(),
                createdUser.getStatus()
        );
    }

    public AuthResponse login(LoginRequest request) {
        String username = request.getEmail();
        String password = request.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(token);

        return new AuthResponse(authentication.getName());
    }
}
