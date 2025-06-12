package com.portfolio.portfolio.ddd.domain.model.vo;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Price {
    private BigDecimal amount;
    private String currency;

    public Price(BigDecimal amount, String currency) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("가격은 0보다 커야 합니다");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public Price() {

    }

    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }

    // equals, hashCode 구현 (불변 객체)
}