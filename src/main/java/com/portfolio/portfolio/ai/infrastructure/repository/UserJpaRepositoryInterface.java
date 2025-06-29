package com.portfolio.portfolio.ai.infrastructure.repository;

import com.portfolio.portfolio.ai.domain.model.entity.AIUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepositoryInterface extends JpaRepository<AIUser, Long> {
    Optional<AIUser> findByUsername(String username);
    Optional<AIUser> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
