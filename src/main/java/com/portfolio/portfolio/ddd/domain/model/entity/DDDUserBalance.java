package com.portfolio.portfolio.ddd.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
public class DDDUserBalance {
    @Id
    private Long id;

    @ManyToOne
    private DDDUser DDDUser;

    private String currency;
    private BigDecimal amount;

    // User를 통해서만 호출됨
    void withdraw(BigDecimal withdrawAmount) {
        if (amount.compareTo(withdrawAmount) < 0) {
            throw new IllegalArgumentException("잔고 부족");
        }
        this.amount = amount.subtract(withdrawAmount);
    }

    void deposit(BigDecimal depositAmount) {
        this.amount = amount.add(depositAmount);
    }
}