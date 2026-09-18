package com.sankalp.quickbite.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCartItemRequest {

    @NotNull
    private Long menuItemId;

    @NotNull
    @Min(1)
    private Long quantity;
}
