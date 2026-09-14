package com.sankalp.quickbite.auth.dto;

import com.sankalp.quickbite.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    private UserRole role;
}
