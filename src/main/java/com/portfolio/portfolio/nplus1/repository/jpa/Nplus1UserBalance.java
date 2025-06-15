package com.portfolio.portfolio.nplus1.repository.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Table(name = "nplus1_user_balances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Nplus1UserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID balanceId;

    @Column(name = "available_balance", nullable = false, precision = 38, scale = 2)
    private BigDecimal availableBalance;

    @Column(name = "total_balance", nullable = false, precision = 38, scale = 2)
    private BigDecimal totalBalance;

    @Column(name = "wallet", nullable = false)
    private String wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Nplus1User nplus1User;
}
