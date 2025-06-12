package com.portfolio.portfolio.ddd.application;

import com.portfolio.portfolio.ddd.domain.OrderType;

import java.math.BigDecimal;

public class CreateOrderCommand {
    private final Long userId;
    private final OrderType type;
    private final String symbol;
    private final BigDecimal price;
    private final BigDecimal quantity;

    public CreateOrderCommand(Long userId, OrderType type, String symbol,
                              BigDecimal price, BigDecimal quantity) {
        this.userId = userId;
        this.type = type;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getUserId() { return userId; }
    public OrderType getType() { return type; }
    public String getSymbol() { return symbol; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getQuantity() { return quantity; }
}
