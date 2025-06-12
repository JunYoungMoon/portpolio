package com.portfolio.portfolio.ddd.domain.model.entity;

import com.portfolio.portfolio.ddd.domain.OrderStatus;
import com.portfolio.portfolio.ddd.domain.OrderType;
import com.portfolio.portfolio.ddd.domain.model.vo.Price;
import com.portfolio.portfolio.ddd.domain.model.vo.Quantity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class DDDOrder {
    @Id
    private Long orderId;
    private Long userId;
    private OrderType type; // BUY, SELL
    private String symbol; // BTC/USDT
    private Price price;
    private Quantity quantity;
    private OrderStatus status;

    public DDDOrder(Long userId, OrderType type, String symbol, Price price, Quantity quantity) {
        this.userId = userId;
        this.type = type;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.status = OrderStatus.PENDING;
    }

    public void cancel() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("대기 중인 주문만 취소할 수 있습니다");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void execute() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("대기 중인 주문만 체결할 수 있습니다");
        }
        this.status = OrderStatus.EXECUTED;
    }
}