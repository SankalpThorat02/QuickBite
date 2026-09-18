package com.sankalp.quickbite.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemResponse {

    private Long menuItemId;
    private String name;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
