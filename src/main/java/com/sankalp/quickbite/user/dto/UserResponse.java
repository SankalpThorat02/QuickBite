package com.sankalp.quickbite.user.dto;

import com.sankalp.quickbite.user.entity.UserRole;
import com.sankalp.quickbite.user.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String name;
    private String email;

    private UserRole role;
    private UserStatus status;
}
