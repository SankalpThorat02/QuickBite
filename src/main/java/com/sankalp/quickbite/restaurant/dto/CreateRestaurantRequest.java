package com.sankalp.quickbite.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateRestaurantRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;
}
