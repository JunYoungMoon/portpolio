package com.portfolio.portfolio.ddd.presentation.dto;

import java.math.BigDecimal;

public class CreateOrderRequest {
    private Long userId;
    private String type;
    private String symbol;
    private BigDecimal price;
    private BigDecimal quantity;

    // getters, setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}