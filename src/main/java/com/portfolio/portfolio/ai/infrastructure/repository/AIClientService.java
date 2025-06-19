package com.portfolio.portfolio.ai.infrastructure.repository;

import com.portfolio.portfolio.ai.domain.model.AIPrompt;
import com.portfolio.portfolio.ai.domain.repository.AIClientRepository;
import org.springframework.stereotype.Component;

@Component
public class AIClientService implements AIClientRepository {

    // TODO: AI 클라이언트 구현 (OpenAI, Claude, etc.)
    private final String apiUrl;
    private final String apiKey;

    public AIClientService() {
        // TODO: 설정에서 주입받도록 수정
        this.apiUrl = "AI_API_URL";
        this.apiKey = "AI_API_KEY";
    }

    @Override
    public String sendRequest(AIPrompt aiPrompt) {
        // TODO: 실제 AI API 호출 구현
        // 1. HTTP 클라이언트 설정
        // 2. 요청 body 구성
        // 3. API 호출
        // 4. 응답 파싱
        // 5. 에러 처리

        // 임시 구현
        return "AI 응답: " + aiPrompt.getUserCommand() + "에 대한 처리가 완료되었습니다.";
    }

    @Override
    public boolean isAvailable() {
        // TODO: AI 서비스 상태 확인
        return true;
    }
}
