package com.sankalp.quickbite.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
