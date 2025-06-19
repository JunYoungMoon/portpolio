package com.portfolio.portfolio.ai.domain.repository;

import com.portfolio.portfolio.ai.domain.model.AIPrompt;

public interface AIClientRepository {
    String sendRequest(AIPrompt aiPrompt);
    boolean isAvailable();
}
