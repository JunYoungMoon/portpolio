package com.portfolio.portfolio.ddd.domain.repository;

import com.portfolio.portfolio.ddd.domain.model.entity.DDDOrder;

import java.util.List;

public interface OrderRepository {
    DDDOrder findById(Long orderId);
    void save(DDDOrder DDDOrder);
    List<DDDOrder> findPendingOrdersBySymbol(String symbol);
    List<DDDOrder> findByUserId(Long userId);
}
