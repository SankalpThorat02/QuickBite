package com.sankalp.quickbite.restaurant.dto;

import com.sankalp.quickbite.restaurant.entity.RestaurantStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantStatusUpdateRequest {

    @NotNull
    private RestaurantStatus status;
}
