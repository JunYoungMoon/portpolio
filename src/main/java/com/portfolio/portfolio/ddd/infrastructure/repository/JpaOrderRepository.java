package com.portfolio.portfolio.ddd.infrastructure.repository;

import com.portfolio.portfolio.ddd.domain.OrderStatus;
import com.portfolio.portfolio.ddd.domain.model.entity.DDDOrder;
import com.portfolio.portfolio.ddd.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOrderRepository extends JpaRepository<DDDOrder, Long>{
    List<DDDOrder> findBySymbolAndStatus(String symbol, OrderStatus status);
    List<DDDOrder> findByUserId(Long userId);
}