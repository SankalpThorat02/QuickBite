package com.sankalp.quickbite.user.service;

import com.sankalp.quickbite.user.dto.ChangePasswordRequest;
import com.sankalp.quickbite.user.dto.StatusUpdateRequest;
import com.sankalp.quickbite.user.dto.UpdateUserInfoRequest;
import com.sankalp.quickbite.user.dto.UserResponse;
import com.sankalp.quickbite.user.entity.User;
import com.sankalp.quickbite.user.entity.UserStatus;
import com.sankalp.quickbite.user.exception.IncorrectPasswordProvidedException;
import com.sankalp.quickbite.user.exception.UserNotFoundException;
import com.sankalp.quickbite.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));

        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }

    public UserResponse updateInfo(UpdateUserInfoRequest request) {
        String username = request.getUsername();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new  UserNotFoundException("User with email: " + authentication.getName() + " not found"));

        user.setName(username);

        userRepository.save(user);

        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }

    public void changePassword(ChangePasswordRequest request) {
        String currentPassword = request.getCurrentPassword();
        String newPassword = request.getNewPassword();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new  UserNotFoundException("User with email: " + email + " not found"));

        boolean passwordMatch = passwordEncoder.matches(currentPassword, user.getPasswordHash());

        if(!passwordMatch) {
            throw new IncorrectPasswordProvidedException("Invalid Current Password");
        }

        String newEncodedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(newEncodedPassword);

        userRepository.save(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserResponse(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getStatus()
                )).collect(Collectors.toList());
    }

    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found"));

        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }

    public UserResponse updateUserStatus(Long userId, StatusUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new  UserNotFoundException("User with ID: " + userId + " not found"));

        UserStatus userStatus = request.getStatus();

        user.setStatus(userStatus);
        userRepository.save(user);

        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }
}
