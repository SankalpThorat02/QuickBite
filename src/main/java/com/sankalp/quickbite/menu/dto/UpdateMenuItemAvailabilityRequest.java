package com.sankalp.quickbite.menu.dto;

import com.sankalp.quickbite.menu.entity.MenuItemAvailability;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateMenuItemAvailabilityRequest {

    @NotNull
    private MenuItemAvailability menuItemAvailability;
}
