package com.sankalp.quickbite.user.dto;

import com.sankalp.quickbite.user.entity.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusUpdateRequest {

    @NotBlank
    private UserStatus status;
}
