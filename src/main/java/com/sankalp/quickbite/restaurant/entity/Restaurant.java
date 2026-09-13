package com.sankalp.quickbite.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "RESTAURANTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESTAURANT_SEQ")
    @SequenceGenerator(
            name = "RESTAURANT_SEQ",
            sequenceName = "RESTAURANT_SEQ",
            allocationSize = 1
    )
    @Column(name = "RESTAURANT_ID", nullable = false)
    private Long restaurantId;

    @Column(name = "OWNER_ID", nullable = false)
    private Long ownerId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private RestaurantStatus status;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
}
