package com.portfolio.portfolio.ddd.domain.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DDDUser {
    @Id
    private Long userId;
    private String email;

    @OneToMany(mappedBy = "DDDUser", cascade = CascadeType.ALL)
    private List<DDDUserBalance> balances = new ArrayList<>();

    // 애그리거트 루트를 통한 접근
    public void withdraw(String currency, BigDecimal amount) {
        DDDUserBalance balance = findBalance(currency);
        balance.withdraw(amount);
    }

    public void deposit(String currency, BigDecimal amount) {
        DDDUserBalance balance = findBalance(currency);
        balance.deposit(amount);
    }

    private DDDUserBalance findBalance(String currency) {
        return balances.stream()
                .filter(b -> b.getCurrency().equals(currency))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잔고를 찾을 수 없습니다"));
    }
}