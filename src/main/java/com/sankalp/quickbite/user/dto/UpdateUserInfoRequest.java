package com.sankalp.quickbite.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserInfoRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;
}
