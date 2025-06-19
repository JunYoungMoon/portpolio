package com.portfolio.portfolio.ai.domain.service;

import com.portfolio.portfolio.ai.domain.model.AIPrompt;
import com.portfolio.portfolio.ai.domain.model.VoiceCommand;
import com.portfolio.portfolio.ai.domain.repository.AIClientRepository;
import org.springframework.stereotype.Service;

@Service
public class VoiceProcessingService {

    private final AIPromptService aiPromptService;
    private final AIClientRepository aiClientRepository;

    public VoiceProcessingService(AIPromptService aiPromptService,
                                  AIClientRepository aiClientRepository) {
        this.aiPromptService = aiPromptService;
        this.aiClientRepository = aiClientRepository;
    }

    public String processCommand(VoiceCommand voiceCommand) {
        // 도메인 규칙 검증
        if (!voiceCommand.hasContent()) {
            throw new IllegalArgumentException("처리할 음성 명령이 없습니다.");
        }

        // AI 프롬프트 생성
        AIPrompt aiPrompt = aiPromptService.createPrompt(voiceCommand);

        // AI 요청 처리
        return aiClientRepository.sendRequest(aiPrompt);
    }
}