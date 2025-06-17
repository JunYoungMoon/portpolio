package com.portfolio.portfolio.ai.infrastructure.repository;

import com.portfolio.portfolio.ai.domain.VoiceHistoryRepository;
import com.portfolio.portfolio.ai.domain.model.entity.VoiceHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VoiceHistoryJpaRepository implements VoiceHistoryRepository {

    private final VoiceHistoryRepositoryJpaInterface jpaRepository;

    public VoiceHistoryJpaRepository(VoiceHistoryRepositoryJpaInterface jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<VoiceHistory> findByUsernameOrderByCreatedAtDesc(String username) {
        return jpaRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    @Override
    public List<VoiceHistory> findTop10ByUsernameOrderByCreatedAtDesc(String username) {
        return jpaRepository.findByUsernameOrderByCreatedAtDesc(username);
    }
}
