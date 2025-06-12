package com.portfolio.portfolio.ddd.infrastructure;

import com.portfolio.portfolio.ddd.domain.OrderStatus;
import com.portfolio.portfolio.ddd.domain.model.entity.DDDOrder;
import com.portfolio.portfolio.ddd.domain.repository.OrderRepository;
import com.portfolio.portfolio.ddd.infrastructure.repository.JpaOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("orderRepositoryImpl") //TODO 왜 이름을 명시하지 않고 Impl과 구분해서 둬야만(JPAConfig에서 repository를 보고있음) 순환 참조가 발생하지 않는건가?
public class JpaOrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    public JpaOrderRepositoryImpl(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public DDDOrder findById(Long orderId) {
        return jpaOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다"));
    }

    @Override
    public void save(DDDOrder DDDOrder) {
        jpaOrderRepository.save(DDDOrder);
    }

    @Override
    public List<DDDOrder> findPendingOrdersBySymbol(String symbol) {
        return jpaOrderRepository.findBySymbolAndStatus(symbol, OrderStatus.PENDING);
    }

    @Override
    public List<DDDOrder> findByUserId(Long userId) {
        return jpaOrderRepository.findByUserId(userId);
    }
}
