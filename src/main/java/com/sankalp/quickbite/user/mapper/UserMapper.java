package com.sankalp.quickbite.user.mapper;

import com.sankalp.quickbite.user.dto.UserResponse;
import com.sankalp.quickbite.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }
}
