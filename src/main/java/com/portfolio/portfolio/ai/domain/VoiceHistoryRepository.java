package com.portfolio.portfolio.ai.domain;

import com.portfolio.portfolio.ai.domain.model.entity.VoiceHistory;

import java.util.List;

public interface VoiceHistoryRepository {
    List<VoiceHistory> findByUsernameOrderByCreatedAtDesc(String username);
    List<VoiceHistory> findTop10ByUsernameOrderByCreatedAtDesc(String username);
}
