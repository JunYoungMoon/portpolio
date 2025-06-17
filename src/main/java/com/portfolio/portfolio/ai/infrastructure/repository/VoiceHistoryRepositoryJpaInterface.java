package com.portfolio.portfolio.ai.infrastructure.repository;


import com.portfolio.portfolio.ai.domain.model.entity.VoiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoiceHistoryRepositoryJpaInterface extends JpaRepository<VoiceHistory, Long> {
    List<VoiceHistory> findByUsernameOrderByCreatedAtDesc(String username);
    List<VoiceHistory> findTop10ByUsernameOrderByCreatedAtDesc(String username);
}
