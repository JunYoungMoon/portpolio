package com.portfolio.portfolio.ddd.infrastructure.repository;

import com.portfolio.portfolio.ddd.domain.model.entity.DDDUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaUserRepository extends JpaRepository<DDDUser, Long> {
    List<DDDUser> findByEmail(String email);
}