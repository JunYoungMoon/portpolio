package com.portfolio.portfolio.ddd.domain.model.vo;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Quantity {
    private BigDecimal value;

    public Quantity(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다");
        }
        this.value = value;
    }

    public Quantity() {

    }

    public BigDecimal getValue() { return value; }
}
