package com.portfolio.portfolio.giftorder.repository;

import com.portfolio.portfolio.giftorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderNumber(String orderNumber);
}
