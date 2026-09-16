package com.sankalp.quickbite.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateRestaurantInfoRequest {

    @NotNull
    private String name;

    @NotNull
    private String address;
}
