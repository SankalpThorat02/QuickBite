package com.sankalp.quickbite.cart.entity;

import com.sankalp.quickbite.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARTS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CART_SEQ")
    @SequenceGenerator(
            name = "CART_SEQ",
            sequenceName = "CART_SEQ",
            allocationSize = 1
    )
    @Column(name = "CART_ID", nullable = false)
    private Long cartId;

    @OneToMany(mappedBy = "cart",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    public void addItem(CartItem cartItem) {
        items.add(cartItem);
        cartItem.setCart(this);
    }

    public void removeItem(CartItem cartItem) {
        items.remove(cartItem);
        cartItem.setCart(null);
    }
}
