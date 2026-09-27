package com.sankalp.quickbite.order.repository;

import com.sankalp.quickbite.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
