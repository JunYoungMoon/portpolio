package com.portfolio.portfolio.ai.domain.service;

import com.portfolio.portfolio.ai.domain.model.AIPrompt;
import com.portfolio.portfolio.ai.domain.model.VoiceCommand;
import org.springframework.stereotype.Service;

@Service
public class AIPromptService {

    public AIPrompt createPrompt(VoiceCommand voiceCommand) {
        return AIPrompt.create(voiceCommand.getText(), voiceCommand.getUsername());
    }

    public AIPrompt createCustomPrompt(String userCommand, String customContext) {
        // 커스텀 프롬프트 생성 로직
        return AIPrompt.create(userCommand, customContext);
    }
}
