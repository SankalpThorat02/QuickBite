package com.sankalp.quickbite.restaurant.entity;

import com.sankalp.quickbite.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "RESTAURANTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private User owner;

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
