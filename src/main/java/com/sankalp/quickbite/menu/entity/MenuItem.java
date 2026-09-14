package com.sankalp.quickbite.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "MENU_ITEMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENU_ITEM_SEQ")
    @SequenceGenerator(
            name = "MENU_ITEM_SEQ",
            sequenceName = "MENU_ITEM_SEQ",
            allocationSize = 1
    )
    @Column(name = "MENU_ITEM_ID", nullable = false)
    private long menuItemId;

    @Column(name = "RESTAURANT_ID", nullable = false)
    private long restaurantId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "AVAILABILITY", nullable = false)
    private MenuItemAvailability availability;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
}
