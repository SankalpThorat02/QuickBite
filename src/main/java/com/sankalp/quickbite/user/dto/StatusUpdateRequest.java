package com.sankalp.quickbite.user.dto;

import com.sankalp.quickbite.user.entity.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusUpdateRequest {

    @NotNull
    private UserStatus status;
}
