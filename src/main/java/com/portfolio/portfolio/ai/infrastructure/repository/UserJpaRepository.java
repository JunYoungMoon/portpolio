package com.portfolio.portfolio.ai.infrastructure.repository;

import com.portfolio.portfolio.ai.domain.model.entity.AIUser;
import com.portfolio.portfolio.ai.domain.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserJpaRepository implements UserRepository {

    private final UserJpaRepositoryInterface jpaRepository;

    public UserJpaRepository(UserJpaRepositoryInterface jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<AIUser> findByUsername(String username) {
        return jpaRepository.findByUsername(username);
    }

    @Override
    public Optional<AIUser> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public Optional<AIUser> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public AIUser save(AIUser AIUser) {
        return jpaRepository.save(AIUser);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}