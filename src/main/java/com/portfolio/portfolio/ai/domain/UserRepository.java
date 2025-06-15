package com.portfolio.portfolio.ai.domain;

import com.portfolio.portfolio.ai.domain.model.entity.AIUser;

import java.util.Optional;

public interface UserRepository {
    Optional<AIUser> findByUsername(String username);
    Optional<AIUser> findByEmail(String email);
    Optional<AIUser> findById(Long id);
    AIUser save(AIUser AIUser);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}