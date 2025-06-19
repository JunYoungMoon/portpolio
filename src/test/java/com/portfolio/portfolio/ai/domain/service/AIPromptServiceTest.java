package com.portfolio.portfolio.ai.domain.service;

import com.portfolio.portfolio.ai.domain.model.AIPrompt;
import com.portfolio.portfolio.ai.domain.model.VoiceCommand;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AIPromptServiceTest {
    @Test
    void AI_프롬프트_생성_테스트() {
        // Given
        VoiceCommand command = VoiceCommand.create("날씨 알려줘", "user", "2025-06-20T10:00:00");
        AIPromptService service = new AIPromptService();

        // When
        AIPrompt prompt = service.createPrompt(command);

        // Then
        assertThat(prompt.getUserCommand()).isEqualTo("날씨 알려줘");
        assertThat(prompt.getPrompt()).contains("사용자 'user'의 음성 명령입니다");
    }
}