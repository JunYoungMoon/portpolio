package com.portfolio.portfolio.nplus1.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Nplus1UserBalanceRepository extends JpaRepository<Nplus1UserBalance, UUID> {
}